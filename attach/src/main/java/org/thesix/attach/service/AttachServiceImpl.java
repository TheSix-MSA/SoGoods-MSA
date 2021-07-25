package org.thesix.attach.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.netflix.discovery.converters.Auto;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;
import org.thesix.attach.dto.UuidRequestDTO;
import org.thesix.attach.dto.UuidResponseDTO;
import org.thesix.attach.entity.Attach;
import org.thesix.attach.repository.AttachRepository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;


@Service
@RequiredArgsConstructor
@Log4j2
public class AttachServiceImpl implements AttachService {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Value("${cloud.aws.host}")
    private String awsHost;

    private final AttachRepository attachRepository;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    @Override
    public List<UuidResponseDTO> uplaodtemp(MultipartFile[] files, String tableName, String keyValue, Integer mainIdx) {
        List<UuidResponseDTO> list = new ArrayList<>();
        log.info("Arrays.toString(files)" + Arrays.toString(files));
        if(files == null)return null;
        for (int i=0; i<files.length; i++) {
            MultipartFile file = files[i];

            if (file.getContentType()==null || file.getContentType().startsWith("image") == false) {
              throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }

            //브라우저별 파일 오리지널파일이름 처리
            String originalName = file.getOriginalFilename();
            int pos = originalName.lastIndexOf("\\") + 1;
            String fileName = originalName.substring(pos);

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"
            String commonPath = uploadPath + File.separator + "temp" + File.separator;
            String uuidFileName = uuid + "_" + fileName;

            String severSideOriginFilePath = commonPath + uuidFileName;
            String serverSideThumbnailFilePath = commonPath + "s_" + uuidFileName;
            Path savePath = Paths.get(severSideOriginFilePath);

            try {
                //서버에 저장
                //  1. 원본
                file.transferTo(savePath);
                //  2. 썸네일

                File thumbnailFile = new File(serverSideThumbnailFilePath);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 130, 130);

            } catch (IOException e) {
                throw new InternalException();
            }//원본, 썸네일 파일 저장 완료...........................

            //tempFile은 uuid + 원본파일명 의 값을 갖는다.
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            String[] pathList = new String[]{severSideOriginFilePath, serverSideThumbnailFilePath};
            for (String ele : pathList) {
                File eleFile = new File(ele);
                FileItem fileItem = null;
                try {
                    fileItem = new DiskFileItem("mainFile", Files.probeContentType(eleFile.toPath()), false, eleFile.getName(), (int) eleFile.length(), eleFile.getParentFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream input=null;
                OutputStream os=null;
                try {
                    input = new FileInputStream(eleFile);
                    os = fileItem.getOutputStream();
                    IOUtils.copy(input, os);
                    // Or faster..
                    // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                } catch (IOException ex) {
                    // do something.
                    throw new InternalException();
                }finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        throw new InternalException("file -> multipart ");
                    }
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                try {
                    s3Client.putObject(new PutObjectRequest(bucket, multipartFile.getOriginalFilename(), multipartFile.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                } catch (IOException e) {
                    throw new InternalException("S3 INSERT 문제 발생");
                }
            }

            Attach.AttachBuilder attachBuilder = Attach.builder()
                    .originalName(uuidFileName)
                    .uuid(uuid);

            if (mainIdx != null && mainIdx==i)
                attachBuilder = attachBuilder.main(true);
            else
                attachBuilder = attachBuilder.main(false);

            attachBuilder = attachBuilder.tableName(tableName);
            attachBuilder = attachBuilder.keyValue(keyValue);

            Attach attach = attachBuilder.build();

            //DB에 INSERT
            Attach save = attachRepository.save(attach);
            list.add(entityToDTO(save));

            try {
                Files.delete(Path.of(severSideOriginFilePath));
            } catch (IOException e) {
                throw new InternalException("임시저장 파일 삭제 오류 ");
            }
            try {
                Files.delete(Path.of(serverSideThumbnailFilePath));
            } catch (IOException e) {
                throw new InternalException("임시저장 썸네일 파일 삭제 오류 ");
            }


        }
        return list;

    }

    @Override
    @Transactional
    public List<UuidResponseDTO> registerConfimedImages(AttachConfimRequestDTO requestDTO) throws IOException {
        String tableName = requestDTO.getTableName();
        String keyValue = requestDTO.getKeyValue();
        String mainFileName = requestDTO.getMainFileName();

        List<UuidResponseDTO> list = new ArrayList<>();

        for (String tempFileName : requestDTO.getTempFileNameList()) {
            log.info(tempFileName);

            //tempFile은 uuid + 원본파일명 의 값을 갖는다.
            String[] arr = URLDecoder.decode(tempFileName, "UTF-8").split("_");

            String decodedFileName = URLDecoder.decode(tempFileName, "UTF-8");

            String uuid = decodedFileName.split("_")[0];
            String fileName = decodedFileName.split("_")[1];
            String ext = fileName.substring(fileName.lastIndexOf(".") + 1);

            //저장할 파일 이름 중간에 "_"
            String tempPath = uploadPath + File.separator + "temp" + File.separator + decodedFileName;
            String s_tempPath = uploadPath + File.separator + "temp" + File.separator + "s_" + decodedFileName;

            String[] pathList = new String[]{tempPath, s_tempPath};
            for (String ele : pathList) {
//                File file = new File(ele);
//                DiskFileItem fileItem = new DiskFileItem(file.getName(), "img/"+ext, false, file.getName(), (int) file.length() , file.getParentFile());
//                fileItem.getOutputStream();


                File file = new File(ele);
                FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
                InputStream input=null;
                OutputStream os=null;
                try {
                    input = new FileInputStream(file);
                    os = fileItem.getOutputStream();
                    IOUtils.copy(input, os);
                    // Or faster..
                    // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                } catch (IOException ex) {
                    // do something.
                }finally {
                    input.close();
                    os.close();
                }

                MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
                s3Client.putObject(new PutObjectRequest(bucket, multipartFile.getOriginalFilename(), multipartFile.getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }

            Attach.AttachBuilder attachBuilder = Attach.builder()
                    .originalName(tempFileName)
                    .uuid(uuid);

            if (mainFileName != null && mainFileName.equals(tempFileName))
                attachBuilder = attachBuilder.main(true);
            else
                attachBuilder = attachBuilder.main(false);

            attachBuilder = attachBuilder.tableName(tableName);
            attachBuilder = attachBuilder.keyValue(keyValue);

            Attach attach = attachBuilder.build();

            //DB에 INSERT
            Attach save = attachRepository.save(attach);



            log.info(tempPath);
            log.info(s_tempPath);
//            File t = new File(tempPath);
//            File s_t = new File(s_tempPath);
//            t.delete();
//            s_t.delete();
            Files.delete(Path.of(tempPath));
            Files.delete(Path.of(s_tempPath));

            /*
            예상결과만 리턴
            실제 원하는 행동은 안하고......
             */
            list.add(entityToDTO(save));
        }
        return list;
    }


    // S3로 옮기면서 더 이상 사용안함
    @Override
    public ResponseEntity<byte[]> getTempFile(String filename) {
        ResponseEntity<byte[]> result = null;

        try {

            String srcFileName = URLDecoder.decode(filename, "UTF-8");

            File file = new File(uploadPath + File.separator + "temp" + File.separator + srcFileName);

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new InternalException();
        }
        return result;
    }

    @Override
    @Transactional
    public void removeFile(String[] fileNames) {

        attachRepository.deleteByOriginalName(fileNames);

        try{
            ArrayList<KeyVersion> keys = new ArrayList<KeyVersion>();
            for (String keyName: fileNames) {
                s3Client.putObject(bucket, keyName, "Object to be deleted.");
                keys.add(new KeyVersion(keyName));
                keys.add(new KeyVersion("s_" + keyName));
            }

            // Delete the sample objects.
            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket)
                    .withKeys(keys)
                    .withQuiet(false);

            // Verify that the objects were deleted successfully.
            DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
            int successfulDeletes = delObjRes.getDeletedObjects().size();
            System.out.println(successfulDeletes + " objects successfully deleted.");

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }

    @Override

    //게시글 목록 : BOARD, 글 keyValues

    //상품 목록: PRODUCT, 상품 keyValues

    public List<UuidResponseDTO> getUuidInBoardList(UuidRequestDTO requestDTO) {

        String type = requestDTO.getType();
        String[] keyValues = requestDTO.getKeyValues();

        if(keyValues.length == 0)throw new IllegalArgumentException("파라미터 키,값에 문제가 있습니다");

        List<Attach> resFromDB = attachRepository.getAttachesByValues(type, keyValues);
        Map<String, Attach> map = new HashMap<>();

        for(Attach el : resFromDB){
            map.put(el.getKeyValue(), el);
        }

        List<UuidResponseDTO> ret = new ArrayList<>();
        for(String keyValue : keyValues){
            if(map.containsKey(keyValue)){
                Attach tmp = map.get(keyValue);
                ret.add(entityToDTO(tmp));
            }
            else {

                Object key = null;
                if(type.equals("MEMBER")){
                    key = keyValue;
                }else{
                    key = Long.parseLong(keyValue);
                }

                UuidResponseDTO dto = UuidResponseDTO.builder()
                        .key(key)
                        .build();
                ret.add(dto);
            }
        }

        return ret;
    }

    @Override
    public List<List<UuidResponseDTO>> getUuidList(UuidRequestDTO requestDTO) {

        String type = requestDTO.getType();
        String[] keyValues = requestDTO.getKeyValues();
        Boolean[] mainList = requestDTO.getMainList();

        if(keyValues.length == 0 || mainList.length == 0)throw new IllegalArgumentException("파라미터 키,값에 문제가 있습니다");

        List<List<UuidResponseDTO>> res = new ArrayList<>();

        for(String keyValue: keyValues){
            List<Attach> part = attachRepository.getAttachesByValue(mainList, type, keyValue);
            res.add(part.stream().map(ele->entityToDTO(ele)).collect(Collectors.toList()));
        }
        return res;
    }


    UuidResponseDTO entityToDTO(Attach attach){

        UuidResponseDTO.UuidResponseDTOBuilder dtoBuilder = UuidResponseDTO.builder();

        if(attach.getTableName().equals("MEMBER")){
            dtoBuilder.key(attach.getKeyValue());
        }else{
            dtoBuilder.key(Long.parseLong(attach.getKeyValue()));
        }

        dtoBuilder.ano(attach.getAno());
        dtoBuilder.fileName(attach.getOriginalName());
        dtoBuilder.main(attach.isMain());
        dtoBuilder.imgSrc(awsHost + "/" + attach.getOriginalName()).build();
        dtoBuilder.thumbSrc(awsHost + "/s_" + attach.getOriginalName()).build();

        return dtoBuilder.build();
    }
}

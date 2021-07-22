package org.thesix.attach.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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
    public void uplaodtemp(MultipartFile[] files, String tableName, String keyValue, Integer mainIdx) {

        for (int i=0; i<files.length; i++) {
            MultipartFile file = files[i];
            System.out.println("file.getContentType(): " + file.getContentType());
            if (file.getContentType().startsWith("image") == false) {
//              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //브라우저별 파일 오리지널파일이름 처리
            String originalName = file.getOriginalFilename();
            int pos = originalName.lastIndexOf("\\") + 1;
            String fileName = originalName.substring(pos);
            log.info(fileName);

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
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

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
                }finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
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
                    e.printStackTrace();
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
            attachRepository.save(attach);

            try {
                Files.delete(Path.of(severSideOriginFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Files.delete(Path.of(serverSideThumbnailFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    @Transactional
    public void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws IOException {
        String tableName = requestDTO.getTableName();
        String keyValue = requestDTO.getKeyValue();
        String mainFileName = requestDTO.getMainFileName();

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
            attachRepository.save(attach);

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
            
        }
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
    public void removeFile(String fileName) {

        attachRepository.deleteByOriginalName(fileName);

        try{
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            s3Client.deleteObject(new DeleteObjectRequest(bucket, "s_" + fileName));
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
        List<Attach> res = attachRepository.getAttachesByValues(type, keyValues);

        return res.stream().map((attach -> entityToDTO(attach))).collect(Collectors.toList());
    }

    @Override
    public List<UuidResponseDTO> getUuidInBoard(UuidRequestDTO requestDTO) {

        String type = requestDTO.getType();
        String keyValue = requestDTO.getKeyValue();
        List<Attach> res = attachRepository.getAttachesByValue(type, keyValue);

        return res.stream().map((attach -> entityToDTO(attach))).collect(Collectors.toList());
    }

    UuidResponseDTO entityToDTO(Attach attach){

        UuidResponseDTO.UuidResponseDTOBuilder dtoBuilder = UuidResponseDTO.builder();

        if(attach.getTableName().equals("MEMBER")){
            dtoBuilder.key(attach.getKeyValue());
        }else{
            dtoBuilder.key(Long.parseLong(attach.getKeyValue()));
        }

        dtoBuilder.imgSrc(awsHost + "/" + attach.getOriginalName()).build();
        dtoBuilder.thumbSrc(awsHost + "/s_" + attach.getOriginalName()).build();

        return dtoBuilder.build();
    }
}

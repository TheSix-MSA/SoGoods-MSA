package org.thesix.attach.service;

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
import javax.transaction.Transactional;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public List<UploadResultDTO> uplaodtemp(MultipartFile[] files) {
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.getContentType().startsWith("image") == false) {
//              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //브라우저별 파일 오리지널파일이름 처리
            String originalName = file.getOriginalFilename();
            int pos = originalName.lastIndexOf("\\") + 1;
            String fileName = originalName.substring(pos);
            log.info(fileName);
            //날짜 폴더 생성
            //String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"
            String commonPath = uploadPath + File.separator + "temp" + File.separator;
            String uuidFileName = uuid + "_" + fileName;

            String severSideOriginFilePath = commonPath + uuidFileName;

            Path savePath = Paths.get(severSideOriginFilePath);

            try {
                //서버에 저장
                //  1. 원본
                file.transferTo(savePath);
                //  2. 썸네일
                String serverSideThumbnailFilePath = commonPath + "s_" + uuidFileName;
                File thumbnailFile = new File(serverSideThumbnailFilePath);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                //응답해줄 업로드 정보 추가
                resultDTOList.add(new UploadResultDTO(uuid, fileName));
            } catch (IOException e) {
                throw new InternalException();
            }
        }

        return resultDTOList;
    }

    @Override
    public void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws IOException {
        String tableName = requestDTO.getTableName();
        Long keyValue = requestDTO.getKeyValue();
        String mainFileName = requestDTO.getMainFileName();
        log.info("mainFIleName: " + mainFileName);
        log.info(requestDTO);
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
            for(String ele : pathList){
//                File file = new File(ele);
//                DiskFileItem fileItem = new DiskFileItem(file.getName(), "img/"+ext, false, file.getName(), (int) file.length() , file.getParentFile());
//                fileItem.getOutputStream();


                File file = new File(ele);
                FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

                try {
                    InputStream input = new FileInputStream(file);
                    OutputStream os = fileItem.getOutputStream();
                    IOUtils.copy(input, os);
                    // Or faster..
                    // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                } catch (IOException ex) {
                    // do something.
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

            new File(tempPath).delete();
            new File(s_tempPath).delete();
        }
    }

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
    public void removeFile(String opt, String fileName) {

        if (opt.equals("saved")) {
            try {
                attachRepository.deleteByOriginalName(URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            s3Client.deleteObject(new DeleteObjectRequest(bucket, awsHost+"/"+fileName));
            s3Client.deleteObject(new DeleteObjectRequest(bucket, awsHost+"/s_"+fileName));

        }else{
            try {
                String srcFileName = null;

                srcFileName = URLDecoder.decode(fileName);

                File originFile = new File(uploadPath + File.separator + "temp" + File.separator + srcFileName);
                File thumnailFile = new File(uploadPath + File.separator + "temp" + File.separator + "s_" + srcFileName);

                originFile.delete();
                thumnailFile.delete();
            } catch (Exception e) {
                throw new InternalException();
            }
        }
    }

    @Override
    public List<UuidResponseDTO> getUuidInBoardList(UuidRequestDTO requestDTO) {

        String type = requestDTO.getType();
        long[] keyValues = requestDTO.getKeyValues();

        List<Attach> res = attachRepository.getAttachesByValues(type, keyValues);

        return res.stream().map((attach -> entityToDTO(attach))).collect(Collectors.toList());
    }

    @Override
    public List<UuidResponseDTO> getUuidInBoard(UuidRequestDTO requestDTO) {

        String type = requestDTO.getType();
        long keyValue = requestDTO.getKeyValue();
        List<Attach> res = attachRepository.getAttachesByValue(type, keyValue);

        return res.stream().map((attach -> entityToDTO(attach))).collect(Collectors.toList());
    }

    UuidResponseDTO entityToDTO(Attach attach){
        UuidResponseDTO dto = UuidResponseDTO.builder()
                .key(attach.getKeyValue())
                .fileFullName(awsHost + "/" + attach.getOriginalName())
                .build();
        return dto;
    }
}

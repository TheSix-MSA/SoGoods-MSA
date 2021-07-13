package org.thesix.attach.service;

import com.sun.jdi.InternalException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;
import org.thesix.attach.entity.Attach;
import org.thesix.attach.repository.AttachRepository;

import javax.swing.*;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class AttachServiceImpl implements AttachService{

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final AttachRepository attachRepository;

    @Override
    public List<UploadResultDTO> uplaodtemp(MultipartFile[] files) {
        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile file : files){

            if(file.getContentType().startsWith("image") == false) {
//              return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            }

            //브라우저별 파일 오리지널파일이름 처리
            String originalName = file.getOriginalFilename();
            int pos =  originalName.lastIndexOf("\\") + 1;
            String fileName = originalName.substring(pos);
            log.info(fileName);
            //날짜 폴더 생성
            //String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"
            String commonPath = uploadPath + File.separator + "temp" + File.separator;
            String uuidFileName =  uuid + "_" + fileName;

            String severSideOriginFilePath = commonPath + uuidFileName;

            Path savePath = Paths.get(severSideOriginFilePath);

            try{
                //서버에 저장
                //  1. 원본
                file.transferTo(savePath);
                //  2. 썸네일
                String serverSideThumbnailFilePath = commonPath + "s_" + uuidFileName;
                File thumbnailFile = new File(serverSideThumbnailFilePath);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                //응답해줄 업로드 정보 추가
                resultDTOList.add(new UploadResultDTO(uuid, fileName));
            }catch (IOException e){
                throw new InternalException();
            }
        }

        return resultDTOList;
    }

    @Override
    public void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws UnsupportedEncodingException {
        String tableName = requestDTO.getTableName();
        Long keyValue = requestDTO.getKeyValue();
        String mainFileName = requestDTO.getMainFileName();
        log.info(requestDTO);
        for(String tempFileName : requestDTO.getTempFileNameList()){
            //tempFile은 uuid + 원본파일명 의 값을 갖는다.
            String[] arr = URLDecoder.decode(tempFileName, "UTF-8").split("_");

            String decodedFileName = URLDecoder.decode(tempFileName, "UTF-8");
            String uuid = decodedFileName.split("_")[0];
            String fileName = decodedFileName.split("_")[1];

            //날짜 폴더 생성
            //String folderPath = makeFolder()

            //저장할 파일 이름 중간에 "_"
            String tempPath  = uploadPath + File.separator + "temp" + File.separator + decodedFileName;
            String savedPath = uploadPath + File.separator + "saved" + File.separator + decodedFileName;

            String s_tempPath  = uploadPath + File.separator + "temp"  + File.separator + "s_" + decodedFileName;
            String s_savedPath = uploadPath + File.separator + "saved" + File.separator + "s_" + decodedFileName;

            //파일삭제(원본, 썸네일)
            try {
                Files.copy(Paths.get(tempPath), Paths.get(savedPath));
                Files.copy(Paths.get(s_tempPath), Paths.get(s_savedPath));
                new File(tempPath).delete();
                new File(s_tempPath).delete();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Attach.AttachBuilder attachBuilder = Attach.builder()
                    .originalName(tempFileName)
                    .uuid(uuid);

            if(mainFileName.equals(tempFileName))
                attachBuilder = attachBuilder.main(true);
            else
                attachBuilder = attachBuilder.main(false);

            attachBuilder = attachBuilder.tableName(tableName);
            attachBuilder = attachBuilder.keyValue(keyValue);

            Attach attach = attachBuilder.build();

            //DB에 INSERT
            attachRepository.save(attach);
        }
    }

    @Override
    public ResponseEntity<byte[]> getFile(String opt, String filename) {
        ResponseEntity<byte[]> result = null;

        try {

            String srcFileName = URLDecoder.decode(filename, "UTF-8");

            File file = new File(uploadPath + File.separator + opt + File.separator + srcFileName);

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        }catch (Exception e) {
            throw new InternalException();
        }
        return result;
    }

    @Override
    @Transactional
    public void removeTempFile(String opt,String fileName) {
        try{
            String srcFileName = null;

            srcFileName = URLDecoder.decode(fileName);

            File originFile   = new File(uploadPath + File.separator + opt + File.separator + srcFileName);
            File thumnailFile = new File(uploadPath + File.separator + opt + File.separator + "s_" + srcFileName);

            originFile.delete();
            thumnailFile.delete();
        }catch(Exception e){
            throw new InternalException();
        }
        System.out.println("fileName: " + fileName);
        if(opt.equals("saved")){
            try {
                attachRepository.deleteByOriginalName(URLEncoder.encode(fileName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }




}

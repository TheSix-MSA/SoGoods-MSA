package org.thesix.attach.controller;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 도원진
 */
@RestController

@Log4j2
@RequestMapping("/attach")
public class UploadController {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;




    /**
     * 클라이언트에서 전종해준 파일의 업로드 처리
     * @param files
     */
    @PostMapping("/uploadTemp")
    public ResponseEntity<List<UploadResultDTO>> uplaodTemp(MultipartFile[] files){

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile file : files){

            if(file.getContentType().startsWith("image") == false) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            //브라우저별 파일 오리지널파일이름 처리
            String originalName = file.getOriginalFilename();
            int pos =  originalName.lastIndexOf("\\") + 1;
            String fileName = originalName.substring(pos);

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
                resultDTOList.add(new UploadResultDTO("temp", uuid, fileName));
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        //클라이언트가 업로드 된 이미지에 접근할 수 있도록 정보 응답
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);

    }

    @PostMapping("/uploadConfirm")
    public ResponseEntity<String> uplaodConfirm(AttachConfimRequestDTO requestDTO) throws IOException {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.info(resultDTOList);
        log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");



        //클라이언트가 업로드 된 이미지에 접근할 수 있도록 정보 응답
        return new ResponseEntity<String>("STRING", HttpStatus.OK);

    }

    /**
     * 클라이언트 이미지 요청에 대한 이미지응답
     * @param filename
     * @return
     */
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String filename){
        ResponseEntity<byte[]> result = null;
        try {
            String srcFileName = URLDecoder.decode(filename, "UTF-8");

            File file = new File(uploadPath + File.separator + srcFileName);

            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }




    /**
     *  연/월 폴더생성
      * @return
     */
    private String makeFolder(){

        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.info("str: " + str);

        String folderPath = str.replace("/", File.separator);
        log.info("folderPath : " + folderPath);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists()){
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}

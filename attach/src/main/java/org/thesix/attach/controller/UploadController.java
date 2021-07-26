package org.thesix.attach.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;
import org.thesix.attach.service.AttachService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 도원진
 */
@RestController
//
@Log4j2
@RequestMapping("/attach")
@RequiredArgsConstructor
public class UploadController {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final AttachService attachService;


    /**
     * 클라이언트에서 전종해준 파일의 업로드 처리
     * @param files
     * URL: http://localhost:8022/attach/upload/temp
     */
    @PostMapping("/upload/temp")
    public ResponseEntity<List<UploadResultDTO>> uplaodtemp(MultipartFile[] files){

        makeFolder();

        List<UploadResultDTO> resultDTOList = attachService.uplaodtemp(files);

        //클라이언트가 업로드 된 이미지에 접근할 수 있도록 정보 응답
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);

    }

    /**
     * 글 등록 확정시 첨부파일을 임시폴더에서 영구폴더로 이동및 데이터베이스 INSERT
     * @param requestDTO
     * @return
     * @throws IOException
     * 요청JSON 포멧은 아래 파라미터 AttachConfimRequestDTO 클래스 주석참고
     */
    @PostMapping("/upload/confirm")
    public ResponseEntity<String> uplaodConfirm(@RequestBody AttachConfimRequestDTO requestDTO) throws IOException {

        attachService.registerConfimedImages(requestDTO);

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        //클라이언트가 업로드 된 이미지에 접근할 수 있도록 정보 응답
        return new ResponseEntity<String>("STRING", HttpStatus.OK);

    }

    /**
     * 클라이언트 이미지 요청에 대한 이미지응답
     * @param filename
     * @return
    opt: ["temp", "saved"] 중 하나
    API URL예시
    http://localhost:8022/attach/display/temp?
        filename=s_b0246ab4-c3f6-43a7-9c3f-616955046ea9_%ED%95%9C%EA%B8%80.png
     */
    @GetMapping("/display/{opt}")
    public ResponseEntity<byte[]> getFile(@PathVariable String opt, String filename){

        ResponseEntity<byte[]> result = attachService.getFile(opt, filename);

        return result;
    }

    @GetMapping("/remove/{opt}")
    public ResponseEntity<Boolean> removeFile(@PathVariable String opt, String fileName){
        System.out.println(opt + " " + fileName);
        attachService.removeTempFile(opt, fileName);
        
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    /**
     *  연/월 폴더생성
      * @return
     */
    private void makeFolder(){

        File uploadPathFolder = new File(uploadPath);
        File tempPathFolder = new File(uploadPath, "temp");
        File savedPathFolder = new File(uploadPath, "saved");

        if(!uploadPathFolder.exists()){
            uploadPathFolder.mkdirs();
        }

        if(!tempPathFolder.exists()){
            tempPathFolder.mkdirs();
        }

        if(!savedPathFolder.exists()){
            savedPathFolder.mkdirs();
        }
    }
}

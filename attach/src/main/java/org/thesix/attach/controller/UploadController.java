package org.thesix.attach.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;
import org.thesix.attach.dto.UuidRequestDTO;
import org.thesix.attach.dto.UuidResponseDTO;
import org.thesix.attach.service.AttachService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.thesix.util.ApiUtil.*;
/**
 * @author 도원진
 */
@RestController
@Log4j2
@RequestMapping("/attach")
@RequiredArgsConstructor
public class UploadController {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;



    private final AttachService attachService;

    /**
     * 클라이언트에서 전종해준 파일의 업로드 처리
     *
     * @param files URL: http://localhost:8022/attach/upload/temp
     */
    @PostMapping("/upload")
    public ApiResult<List<UuidResponseDTO>> uplaodtemp(MultipartFile[] files, String tableName, String keyValue, Integer mainIdx) {
        makeFolder();
        List<UuidResponseDTO> res = null;
        try {
            res = attachService.uplaodtemp(files, tableName, keyValue, mainIdx);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //클라이언트가 업로드 된 이미지에 접근할 수 있도록 정보 응답
        return success(res);
    }

    /**
     * 글 등록 확정시 첨부파일을 임시폴더에서 영구폴더로 이동및 데이터베이스 INSERT
     *
     * @param requestDTO
     * @return
     * @throws IOException 요청JSON 포멧은 아래 파라미터 AttachConfimRequestDTO 클래스 주석참고
     */
    @PostMapping("/upload/confirm")
    public ApiResult<List<UuidResponseDTO>> uplaodConfirm(@RequestBody AttachConfimRequestDTO requestDTO) throws IOException {

        List<UuidResponseDTO> res =  attachService.registerConfimedImages(requestDTO);

        //클라이언트가 업로드 된 이미지에 접근할 수 있도록 정보 응답
        return success(res);

    }

    /**
     * 클라이언트 이미지 요청에 대한 이미지응답
     *
     * @param fileName
     * @return opt: ["temp", "saved"] 중 하나
     * API URL예시
     * http://localhost:8022/attach/display/temp?
     * filename=s_b0246ab4-c3f6-43a7-9c3f-616955046ea9_%ED%95%9C%EA%B8%80.png
     */
    @GetMapping("/display/temp")
    public ResponseEntity<byte[]> getTempFile(String fileName) {

        ResponseEntity<byte[]> result = attachService.getTempFile(fileName);

        return result;
    }


    //s3 삭제
    @DeleteMapping("/remove")
    public ApiResult<Boolean> removeFile(String[] fileNames) {

        attachService.removeFile(fileNames);

        return success(true);
    }

    //s3 조회
    @GetMapping("/list/uuid")
    public ApiResult<List<UuidResponseDTO>> getUuidInList(UuidRequestDTO requestDTO){
        List<UuidResponseDTO> res = attachService.getUuidInBoardList(requestDTO);
        return success(res);
    }

    @DeleteMapping("/detail/uuid")
    public ApiResult<List<UuidResponseDTO>> getUuidInDetail(UuidRequestDTO requestDTO){

        List<UuidResponseDTO> res = attachService.getUuidInBoard(requestDTO);

        return success(res);
    }

    /**
     * 연/월 폴더생성
     *
     * @return
     */
    private void makeFolder() {

        File uploadPathFolder = new File(uploadPath);
        File tempPathFolder = new File(uploadPath, "temp");
        File savedPathFolder = new File(uploadPath, "saved");

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }

        if (!tempPathFolder.exists()) {
            tempPathFolder.mkdirs();
        }
    }
}

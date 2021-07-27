package org.thesix.attach.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;
import org.thesix.attach.dto.UuidRequestDTO;
import org.thesix.attach.dto.UuidResponseDTO;
import org.thesix.attach.entity.Attach;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface AttachService {

    List<UuidResponseDTO> uplaodtemp(MultipartFile[] files, String tableName, String keyValue, Integer mainIdx) throws IOException;

    List<UuidResponseDTO> registerConfimedImages(AttachConfimRequestDTO requestDTO) throws IOException;

    ResponseEntity<byte[]> getTempFile(String filename);

    void removeFile(String[] fileNames);

    List<UuidResponseDTO> getUuidInBoardList(UuidRequestDTO requestDTO);

    List<List<UuidResponseDTO>> getUuidList(UuidRequestDTO requestDTO);



}

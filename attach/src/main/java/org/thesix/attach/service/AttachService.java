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

public interface AttachService {

    List<UploadResultDTO> uplaodtemp(MultipartFile[] files);

    void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws IOException;

    ResponseEntity<byte[]> getTempFile(String filename);

    void removeFile(String opt, String fileName);

    List<UuidResponseDTO> getUuidInBoardList(UuidRequestDTO requestDTO);

    List<UuidResponseDTO> getUuidInBoard(UuidRequestDTO requestDTO);


}

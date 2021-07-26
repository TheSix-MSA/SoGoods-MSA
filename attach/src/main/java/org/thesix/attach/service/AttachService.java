package org.thesix.attach.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.dto.UploadResultDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AttachService {

    List<UploadResultDTO> uplaodtemp(MultipartFile[] files);

    void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws UnsupportedEncodingException;

    ResponseEntity<byte[]> getFile(String opt, String filename);

    void removeTempFile(String opt, String fileName);


}

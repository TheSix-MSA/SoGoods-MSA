package org.thesix.attach.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO {

    private String uuid;
    private String fileName;
    private String fileFullName;

    public UploadResultDTO(String uuid, String fileName) throws UnsupportedEncodingException {
        this.uuid = uuid;
        this.fileName = fileName;
        this.fileFullName = uuid + "_" + URLEncoder.encode(fileName, "UTF-8");
    }
}

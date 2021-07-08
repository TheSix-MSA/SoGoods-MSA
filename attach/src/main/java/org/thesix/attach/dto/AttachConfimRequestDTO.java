package org.thesix.attach.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttachConfimRequestDTO {

    private String tableName;

    private String keyValue;

    private List<String> fileNameList;

    private String mainFileName;
}

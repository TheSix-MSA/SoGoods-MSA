package org.thesix.attach.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttachConfimRequestDTO {

    /*
{
    "tableName": "tbl_board",
    "keyValue": "123",
    "tempFileNameList": [
        "9561eea4-32b7-4871-a1d6-164b1b9622ab_%ED%95%9C%EA%B8%80.png",
        "s_9561eea4-32b7-4871-a1d6-164b1b9622ab_%ED%95%9C%EA%B8%80.png"
    ],
    "mainFileName": "9561eea4-32b7-4871-a1d6-164b1b9622ab_%ED%95%9C%EA%B8%80.png"
}
     */

    private String tableName;

    private Long keyValue;

    private List<String> tempFileNameList;

    private String mainFileName;
}

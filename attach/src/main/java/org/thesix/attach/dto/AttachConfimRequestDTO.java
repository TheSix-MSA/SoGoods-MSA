package org.thesix.attach.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttachConfimRequestDTO {

    /*
{
    "tableName": "REPLY",
    "keyValue": "3",
    "tempFileNameList": [
        "f5f11c37-bd8b-4a77-89c2-84061adfce72_1.png",
        "ccd9aea4-2e64-46d8-89fe-b076b0a4f713_1.jpg"
    ],
    "mainFileName": "ccd9aea4-2e64-46d8-89fe-b076b0a4f713_1.jpg"
}
     */

    private String tableName;

    private Long keyValue;

    private List<String> tempFileNameList;

    private String mainFileName;
}

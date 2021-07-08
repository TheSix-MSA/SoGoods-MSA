package org.thesix.attach.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO {

    private String folderPath;
    private String uuid;
    private String fileName;

    public String getOriginalImageURL(){
        try{
            return URLEncoder.encode(folderPath + "\\" + uuid + "_" + fileName, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbNailImageURL(){
        try{
            return URLEncoder.encode(folderPath + "\\s_" + uuid + "_" + fileName, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}

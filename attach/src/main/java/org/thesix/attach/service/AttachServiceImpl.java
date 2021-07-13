package org.thesix.attach.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.entity.Attach;
import org.thesix.attach.repository.AttachRepository;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Log4j2
public class AttachServiceImpl implements AttachService{

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final AttachRepository attachRepository;

    @Override
    public void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws UnsupportedEncodingException {
        String tableName = requestDTO.getTableName();
        Long keyValue = requestDTO.getKeyValue();
        String mainFileName = requestDTO.getMainFileName();
        log.info(requestDTO);
        for(String tempFileName : requestDTO.getTempFileNameList()){
            //tempFile은 uuid + 원본파일명 의 값을 갖는다.
            String[] arr = URLDecoder.decode(tempFileName, "UTF-8").split("_");

            String decodedFileName = URLDecoder.decode(tempFileName, "UTF-8");
            String uuid = decodedFileName.split("_")[0];
            String fileName = decodedFileName.split("_")[1];

            //날짜 폴더 생성
            //String folderPath = makeFolder()

            //저장할 파일 이름 중간에 "_"
            String tempPath  = uploadPath + File.separator + "temp" + File.separator + decodedFileName;
            String savedPath = uploadPath + File.separator + "saved" + File.separator + decodedFileName;

            String s_tempPath  = uploadPath + File.separator + "temp"  + File.separator + "s_" + decodedFileName;
            String s_savedPath = uploadPath + File.separator + "saved" + File.separator + "s_" + decodedFileName;

            //파일삭제(원본, 썸네일)
            try {
                Files.copy(Paths.get(tempPath), Paths.get(savedPath));
                Files.copy(Paths.get(s_tempPath), Paths.get(s_savedPath));
                new File(tempPath).delete();
                new File(s_tempPath).delete();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Attach.AttachBuilder attachBuilder = Attach.builder()
                    .originalName(tempFileName)
                    .uuid(uuid);

            if(mainFileName.equals(tempFileName))
                attachBuilder = attachBuilder.main(true);
            else
                attachBuilder = attachBuilder.main(false);

            attachBuilder = attachBuilder.tableName(tableName);
            attachBuilder = attachBuilder.keyValue(keyValue);

            Attach attach = attachBuilder.build();

            //DB에 INSERT
            attachRepository.save(attach);
        }
    }
}

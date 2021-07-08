package org.thesix.attach.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.thesix.attach.dto.AttachConfimRequestDTO;
import org.thesix.attach.entity.Attach;
import org.thesix.attach.repository.AttachRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService{

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final AttachRepository attachRepository;

    @Override
    public void registerConfimedImages(AttachConfimRequestDTO requestDTO) {
        String tableName = requestDTO.getTableName();
        String keyValue = requestDTO.getKeyValue();
        String mainFileName = requestDTO.getMainFileName();

        for(String tempFileName : requestDTO.getFileNameList()){
            //tempFile은 uuid + 원본파일명 의 값을 갖는다.
            String[] arr = tempFileName.split("_");
            String uuid = arr[0];
            String originalName = arr[1];

            //날짜 폴더 생성
            //String folderPath = makeFolder()

            //저장할 파일 이름 중간에 "_"
            String tempPath  = uploadPath + File.separator + "temp" + File.separator + tempFileName;
            String savedPath = uploadPath + File.separator + "saved" + File.separator + tempFileName;


            try {
                Files.copy(Paths.get(tempPath), Paths.get(savedPath));
                new File(tempPath).delete();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

//            Attach.Builder attach = Attach.builder()
//                    .originalName(tempFileName)
//                    .uuid(uuid);
//
//            build()
//            attachRepository.save();

        }
    }
}

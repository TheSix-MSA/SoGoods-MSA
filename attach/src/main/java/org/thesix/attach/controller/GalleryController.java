package org.thesix.attach.controller;

import org.springframework.stereotype.Controller;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thesix.attach.dto.GalleryDto;
import org.thesix.attach.service.GalleryService;
import org.thesix.attach.service.S3Service;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class GalleryController {
    private S3Service s3Service;
    private GalleryService galleryService;

    @PostMapping("/gallery")
    public String execWrite(GalleryDto galleryDto, MultipartFile file) throws IOException {
        String imgPath = s3Service.upload(file);
        galleryDto.setFilePath(imgPath);
        galleryService.savePost(galleryDto);
        return "OK";
    }
}

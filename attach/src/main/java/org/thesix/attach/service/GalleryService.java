package org.thesix.attach.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thesix.attach.dto.GalleryDto;
import org.thesix.attach.repository.GalleryRepository;

@Service
@AllArgsConstructor
public class GalleryService {
    private GalleryRepository galleryRepository;

    public void savePost(GalleryDto galleryDto) {
        galleryRepository.save(galleryDto.toEntity());
    }
}
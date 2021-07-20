package org.thesix.attach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.attach.entity.GalleryEntity;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
}

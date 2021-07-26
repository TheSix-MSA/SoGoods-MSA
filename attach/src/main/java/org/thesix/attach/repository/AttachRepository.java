package org.thesix.attach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.thesix.attach.entity.Attach;

import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    @Modifying
    @Query("DELETE FROM Attach a WHERE a.originalName = :originalName")
    void deleteByOriginalName(String originalName);

}

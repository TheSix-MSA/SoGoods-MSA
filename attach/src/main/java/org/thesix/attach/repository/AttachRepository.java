package org.thesix.attach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.attach.entity.Attach;

public interface AttachRepository extends JpaRepository<Attach, Long> {
}

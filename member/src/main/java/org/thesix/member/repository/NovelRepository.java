package org.thesix.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.member.entity.Novels;

public interface NovelRepository extends JpaRepository<Novels,Long> {
}

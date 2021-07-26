package org.thesix.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.Novels;

public interface NovelRepository extends JpaRepository<Novels,Long> {

    @Query("select n from Novels n where n.member = :member and n.deleted = false")
    Page<Novels> getOnesNovels(Pageable pageable,@Param("member") Member member);

}

package org.thesix.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member,String> {
}

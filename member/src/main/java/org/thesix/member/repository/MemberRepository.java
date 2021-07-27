package org.thesix.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.dynamic.MemberSearch;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String>, MemberSearch {

    /**
     * 회원조회
     * @param email
     * @return 회원조회정보
     */
    @EntityGraph(attributePaths = {"roleSet"}, type= EntityGraph.EntityGraphType.FETCH)
    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);

}

package org.thesix.member.repository.dynamic;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.repository.MemberRepository;

import java.util.Arrays;

@SpringBootTest
@Log4j2
class MemberSearchImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Value("${org.secret.key}")
    private String sk;

    @Test
    public void test1(){
        Pageable pageable = PageRequest.of(1, 10);

        Page<Object> members = memberRepository.getMemberList("n", "11", pageable);

        members.getContent().forEach(arr -> log.info("ㅎㅎ"));
    }


    @Test
    public void test2(){

        Member member = memberRepository.findByEmail("aaa102@aaa.aa").orElseThrow(() -> new NullPointerException());

        if(member.getRoleSet().contains(MemberRole.AUTHOR)){
            member.getRoleSet().remove(MemberRole.AUTHOR);
            log.info("지워짐");
            log.info("ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ");
            log.info(member);
        }else{
            member.getRoleSet().add(MemberRole.AUTHOR);
            log.info("추가됨");
        }

        Member save = memberRepository.save(member);
        log.info(save);




    }


}
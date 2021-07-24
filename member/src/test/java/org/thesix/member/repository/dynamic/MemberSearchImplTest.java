package org.thesix.member.repository.dynamic;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.entity.Novels;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.repository.NovelRepository;

import java.util.Arrays;

@SpringBootTest
@Log4j2
class MemberSearchImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NovelRepository novelRepository;

    @Value("${org.secret.key}")
    private String sk;

    @Test
    public void test1(){
        Pageable pageable = PageRequest.of(1, 10);

        Page<Object> members = memberRepository.getMemberList("n", "11", pageable,false);

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

    @Test
    public void registerNovel(){

        Novels novels = Novels.builder()
                .image("https://image.aladin.co.kr/product/61/50/coversum/8970127240_2.jpg")
                .isbn("9788970127248")
                .member(Member.builder().email("aaa100@aaa.aa").build())
                .title("총 균 쇠 (반양장) - 무기.병균.금속은 인류의 운명을 어떻게 바꿨는가, 개정증보판")
                .publisher("문학사상사")
                .build();

        novelRepository.save(novels);
    }

    @Test
    public void approveAuthor(){
        Member member = memberRepository.findByEmail("diqksk@naver.com").orElseThrow(() -> new IllegalArgumentException());

    }

    @Test
    public void test4(){
        Pageable pageable = PageRequest.of(1, 5, Sort.by("nno").descending());

        Page<Novels> onesNovels = novelRepository.getOnesNovels(pageable, Member.builder().email("diqksk@naver.com").build());
        log.info("악앙강ㄱ@@@@@@@@@@@@@@@@@@@@");
        onesNovels.getContent().forEach(novels -> log.info(novels));

    }



}
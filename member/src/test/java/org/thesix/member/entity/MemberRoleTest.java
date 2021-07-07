package org.thesix.member.entity;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thesix.member.repository.MemberRepository;

import java.util.Random;
import java.util.stream.IntStream;


@SpringBootTest
@Log4j2
class MemberRoleTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testInsertMember(){

        IntStream.rangeClosed(1,300).forEach(i->{
            String sex="남자";

            int ran=(int) (Math.random() * 2) + 1;
            if(ran==1){
                sex = "여자";
            }
            int ranDate=(int) (Math.random() * 30) + 1;

            int ranMonth=(int) (Math.random() * 2) + 1;
            int ranYear=(int) (Math.random() * 50) + 60;

            int ranNum=(int) (Math.random() * 9999) + 1;


            String randomBirth = Integer.toString(ranDate) +
                    Integer.toString(ranMonth) +
                    Integer.toString(ranYear);


            Member member = Member.builder()
                    .email("aaa"+i+"@aaa.aa")
                    .name("이름..."+i)
                    .gender(sex)
                    .birth(randomBirth)
                    .phone("010-0000-"+Integer.toString(ranNum))
                    .address("서울시 종로구 냠냠"+i+"동")
                    .detailAddress("i번지")
                    .build();
            member.addMemberRole(MemberRole.GENERAL);

            if(i>100){
                member.addMemberRole(MemberRole.AUTHOR);
            }
            if(i>290){
                member.addMemberRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);


        });

    }
}
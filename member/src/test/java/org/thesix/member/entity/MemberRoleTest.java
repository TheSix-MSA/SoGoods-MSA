package org.thesix.member.entity;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.repository.MemberRepository;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;


@SpringBootTest
@Log4j2
class MemberRoleTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder encoder;

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
            int ranYear=(int) (Math.random() * 49) + 50;
            String ranMonthStr = Integer.toString(ranMonth);
            String ranDateStr = Integer.toString(ranDate);

            if(ranMonthStr.length()<2){
                ranMonthStr = "0" + ranMonthStr;
            }
            if(ranDateStr.length()<2){
                ranDateStr = "0" + ranDateStr;
            }

            int ranNum=(int) (Math.random() * 9999) + 1;

            String randomBirth = Integer.toString(ranYear) +
                    ranMonthStr +
                    ranDateStr;


            Member member = Member.builder()
                    .email("aaa"+i+"@aaa.aa")
                    .password(encoder.encode(""+i))
                    .name("이름..."+i)
                    .gender(sex)
                    .birth(randomBirth)
                    .phone("010-0000-"+Integer.toString(ranNum))
                    .address("서울시 종로구 냠냠"+i+"동")
                    .detailAddress(i+"번지")
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

    @Test
    public void testRead(){

        Optional<Member> member = memberRepository.findByEmail("aaa90@aaa.aa");
        member.ifPresent(member1 -> {
            log.info(member1);
        });


    }

    @Test
    public void testDelete(){
        Member member = Member.builder()
                .email("aaa100@aaa.aa")
                .build();


        memberRepository.delete(member);

    }

    @Test
    public void testModify(){
        Optional<Member> result = memberRepository.findById("aaa101@aaa.aa");

        result.ifPresent(member -> {

            Member temp = Member.builder()
                    .address("수정된 어드레스")
                    .phone("010-0000-0001")
                    .detailAddress("수정된 디테일 어드레스")
                    .build();

            member.changeMemberInfo(temp);

            Member save = memberRepository.save(member);
            log.info(save);

        });



    }



}
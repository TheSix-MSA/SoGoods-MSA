package org.thesix.member.repository.dynamic;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.RefreshToken;
import org.thesix.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

        Page<Object[]> members = memberRepository.getMemberList("n", "11", pageable);

        members.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Transactional
    @Test
    public void test2(){

        Optional<Object[]> result = memberRepository.findByMemberWithRefreshToken("aaa101@aaa.aa");

        List<Object> collect = Arrays.stream(result.get()).collect(Collectors.toList());


    }


}
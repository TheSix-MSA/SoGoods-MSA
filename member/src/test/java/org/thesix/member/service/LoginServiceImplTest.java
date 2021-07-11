package org.thesix.member.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.util.JWTUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@SpringBootTest
@Log4j2
class LoginServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testDecoding(){

        Optional<Member> result = memberRepository.findById("aaa1@aaa.aa");

        result.ifPresent(member ->
        {
            log.info(member.getPassword());
            boolean matches = encoder.matches("2", member.getPassword());

        });

    }

    @Transactional
    @Test
    public void testCreateToken(){

        Optional<Member> result = memberRepository.findById("aaa1@aaa.aa");

        result.ifPresent(member -> {

            List<MemberRole> roleList = member.getRoleSet().stream().collect(Collectors.toList());

            String jwtToken = jwtUtil.generateJWTToken(member.getEmail(), roleList);

            log.info(jwtToken);

            }
        );

    }



}
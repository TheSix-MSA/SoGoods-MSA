package org.thesix.member.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thesix.member.repository.MemberRepository;


@SpringBootTest
@Log4j2
class LoginServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void testHashing(){
        String pass = "1234";
        String encode = encoder.encode(pass);
        log.info(encode);

        boolean check = encoder.matches(encode,pass);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@");
        log.info(check);


    }



}
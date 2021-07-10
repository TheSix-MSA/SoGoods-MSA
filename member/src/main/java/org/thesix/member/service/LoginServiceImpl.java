package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.repository.RefreshTokenRepository;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository tokenRepository;

    @Override
    public TokenDTO Login(LoginInfoDTO dto) {

        Optional<Member> userInfo = memberRepository.findById(dto.getEmail());
//        userInfo.ifPresent();

        return null;
    }
}

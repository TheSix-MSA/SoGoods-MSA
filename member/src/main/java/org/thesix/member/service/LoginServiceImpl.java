package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.repository.RefreshTokenRepository;
import org.thesix.member.util.JWTUtil;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;

    @Override
    public TokenDTO Login(LoginInfoDTO dto) {

        Optional<Member> userInfo = memberRepository.findById(dto.getEmail());
        if(userInfo.isPresent()) {
            Member member = userInfo.get();
            boolean matchResult = encoder.matches(dto.getPassword(), member.getPassword());
            log.info("======>" + matchResult);
            log.info("======>member" + member);
            if (matchResult) {
                String jwtToken = jwtUtil.generateJWTToken(member.getEmail(), member.getRoleSet().stream().collect(Collectors.toList()));

                return TokenDTO.builder().accessToken(jwtToken).build();
            }
        }
        return null;
    }
}

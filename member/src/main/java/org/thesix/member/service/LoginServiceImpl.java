package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.util.JWTUtil;

import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JWTUtil jwtUtil;


    /**
     * 로그인후 JWT발급.
     *
     * @param dto 로그인요청데이터 (ID, PASSWORD)
     * @return (AccessToken, ReFreshToken)
     */
    @Override
    public TokenDTO Login(LoginInfoDTO dto) {

        Member member = memberRepository.findById(dto.getEmail()).orElseThrow(() -> new NullPointerException("해당하는 사용자가 없습니다."));

        boolean matchResult = encoder.matches(dto.getPassword(), member.getPassword());

        if (matchResult) {
            String jwtToken = jwtUtil.generateJWTToken(member.getEmail(), member.getRoleSet().stream().collect(Collectors.toList()));
            String refreshToken = jwtUtil.makeRefreshToken(member.getEmail());

            return TokenDTO.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
        }

        throw new BadCredentialsException("비밀번호가 틀렸습니다.");

    }
}

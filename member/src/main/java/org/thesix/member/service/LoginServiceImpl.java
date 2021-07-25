package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.RefreshToken;
import org.thesix.member.repository.MemberRepository;
import org.thesix.member.repository.RefreshTokenRepository;
import org.thesix.member.util.JWTUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository tokenRepository;
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

            if(member.isRemoved()==true){
                throw new IllegalArgumentException("삭제된 회원입니다.");
            }else if(member.isBanned()==true){
                throw new IllegalArgumentException("정지된 회원입니다.");
            }


            String jwtToken = jwtUtil.generateJWTToken(member.getEmail(), member.getRoleSet().stream().collect(Collectors.toList()));

            String refreshTk = jwtUtil.makeRefreshToken(dto.getEmail());


            return TokenDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshTk)
                    .approval(member.isApproval())
                    .name(member.getName())
                    .email(dto.getEmail())
                    .roles(member.getRoleSet())
                    .build();
        }

        throw new BadCredentialsException("비밀번호가 틀렸습니다.");

    }

    /**
     *
     * [수정중..]
     * 이메일 인증전송
     * 이메일 UI수정필요.
     *
     * @param email
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public String emailVerify(String email) {

        Member member = memberRepository.findById(email).orElse(null);

        if(member != null){
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        String pattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        if(Pattern.matches(pattern,email) == false){
            throw new IllegalArgumentException("올바른 이메일의 형식이 아닙니다.");
        }

        //랜덤한 4자리 숫자 발생
        String randomCode = Integer.toString((int)Math.ceil((Math.random()*8999)+1001));

        log.info("랜덤한 4자리 숫자" + randomCode);
        log.info("인풋 이메일" + email);
        StringBuffer content = new StringBuffer();

        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("<meta charset='UTF-8'>");
        content.append("<meta http-equiv='X-UA-Compatible' content='IE=edge'>");
        content.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div style='display: flex; justify-content: center;'>");
        content.append("<div style='background: #8c7b6c6e; width:600px; height: 300px; padding:30px; border-radius: 30px;' >");
        content.append("<div>");
        content.append("<div>");
        content.append("< h1 style='text-align: center; color: #414141;'><span style='color: #8FAB49;'>SoGoods</span>에 오신것을 환영합니다.</h1>");
        content.append(" </div>");
        content.append("<div>");
        content.append("<div>");
        content.append("</div>");
        content.append("<div><strong>인증번호 : </strong><strong style='color:red'>"+randomCode+"</strong></div>");
        content.append("</div>");
        content.append("</div>");
        content.append("</div>");
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
            message.setTo(email);
            message.setSubject("회원가입 인증 메일입니다.");
            message.setText(content.toString(),true);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("serverError");
        }
        javaMailSender.send(mimeMessage);

        return randomCode;
    }

    @Override
    public TokenDTO refreshToken(TokenDTO token) {

        if (token.getAccessToken() != null && token.getRefreshToken() != null) {
            //토큰을 검증하고 토큰을 재발급한다
            String jwtToken = jwtUtil.generateJWTToken(token.getEmail(), token.getRoles().stream().collect(Collectors.toList()));

            String refreshTk = jwtUtil.makeRefreshToken(token.getEmail());


            return TokenDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshTk)
                    .name(token.getName())
                    .email(token.getEmail())
                    .roles(token.getRoles())
                    .build();
        }

        throw new IllegalArgumentException("재로그인이 필요합니다.");
    }

}

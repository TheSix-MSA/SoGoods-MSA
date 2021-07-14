package org.thesix.member.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.thesix.member.dto.RefreshDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.service.RefreshTokenService;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {

    //토큰 시크릿키
    @Value("${org.secret.key}")
    private String secretKey;

    //토큰 만료시간 (1분)
    private long expiredDate = 20;

    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * 로그인시 이용할 JWT토큰을 발행한다.
     *
     * @param email login Id값 (email)
     * @return email을 JWT token화 하여 문자열로 반납.
     */
    public String generateJWTToken(String email, List<MemberRole> roles) {
        System.out.println("여기는 token:" + secretKey);
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expiredDate).toInstant()))
                .claim("email", email)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     * @param email 인자는 pk값인 email이다.
     * @return RefreshDTO 주어진 이메일로 RefreshToken에 사용할 값을 가지고있는 DTO를 생성한다.
     */
    public String makeRefreshToken(String email) {
        //만료기한은 1달
        System.out.println("여기는 refreshToken:" + secretKey);
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expiredDate + 300).toInstant()))
                .claim("email", email)
                .claim("expireDate", Date.from(ZonedDateTime.now().plusMinutes(expiredDate + 300).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();


    }
}

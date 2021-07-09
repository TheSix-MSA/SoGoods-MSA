package org.thesix.member.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thesix.member.dto.RefreshDTO;
import org.thesix.member.service.RefreshTokenService;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

public class JWTUtil {

    //토큰 시크릿키
    @Value("{org.secret.key}")
    private String secretKey;

    //토큰 만료시간 (1분)
    private long expiredDate = 1;

    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * 로그인시 이용할 JWT토큰을 발행한다.
     *
     * @param content login Id값 (email)
     * @return email을 JWT token화 하여 문자열로 반납.
     */
    public String generateJWTToken(String content){
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expiredDate).toInstant()))
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    /**
     *
     * @param email 인자는 pk값인 email이다.
     * @return RefreshDTO 주어진 이메일로 RefreshToken에 사용할 값을 가지고있는 DTO를 생성한다.
     */
    public RefreshDTO makeRefreshToken(String email){
        //만료기한은 1달
        long expiredDate = System.currentTimeMillis() + (1000 * 60 * 24 * 30); //
        return RefreshDTO.builder()
                .email(email)
                .expireDate(expiredDate)
                .build();
    }


}

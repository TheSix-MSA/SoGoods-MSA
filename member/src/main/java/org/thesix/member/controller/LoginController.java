package org.thesix.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.service.LoginService;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class LoginController {

    private final LoginService loginService;


    /**
     * 로그인 (로그인정보 일치시 JWT토큰과 Refresh토큰 발급)
     * @param dto (Id, Password)
     * @return JWT/Refresh 토큰
     */

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> acceptLogin(@RequestBody LoginInfoDTO dto){
        log.info(dto);
        return ResponseEntity.ok(loginService.Login(dto));
    }
}

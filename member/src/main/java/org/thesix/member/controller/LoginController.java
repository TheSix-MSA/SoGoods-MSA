package org.thesix.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.service.LoginService;

import java.util.HashMap;
import java.util.Map;

import static org.thesix.member.util.ApiUtil.ApiResult;
import static org.thesix.member.util.ApiUtil.success;

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
    public ApiResult<TokenDTO> acceptLogin(@RequestBody LoginInfoDTO dto){
        log.info(dto);
        return success(loginService.Login(dto));
    }

    @PutMapping("/login")
    public ApiResult<Map<String, Object>> verifyEmail(@RequestBody LoginInfoDTO email) {
        log.info(email.getEmail());
        String result = loginService.emailVerify(email.getEmail());
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "확인코드");
        map.put("code", result);

        return success(map);
    }


    /**
     * localstorage에 저장될 dto발급
     *
     * @param token
     * @return
     */
    @PostMapping("/refresh")
    public ApiResult<TokenDTO> refresh(@RequestBody TokenDTO token){

        return success(loginService.refreshToken(token));
    }


}

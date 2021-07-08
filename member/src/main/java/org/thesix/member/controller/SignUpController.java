package org.thesix.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/member")
public class SignUpController {

    private final MemberService memberService;

    /**
     *
     * @param dto 회원가입정보(멤버의 DTO)
     * @return String email 이메일
     */
    @PutMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody MemberDTO dto){

        dto.addMemberRole(MemberRole.GENERAL);
        String email = memberService.regist(dto);

        return ResponseEntity.ok(email);
    }

    @GetMapping("/getUser/{email}")
    public String getUser(@PathVariable("email") String email){

        String jsonUser = memberService.readUser(email);

        return jsonUser;
    }

    @DeleteMapping("/delete/{email}")
    public String delete(@PathVariable("email") String email) {


        return "";
    }



}

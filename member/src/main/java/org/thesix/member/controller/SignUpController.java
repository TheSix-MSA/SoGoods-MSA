package org.thesix.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SignUpController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String SignUp(@RequestBody MemberDTO dto){

        dto.addMemberRole(MemberRole.GENERAL);
        String email = memberService.regist(dto);

        return "{'email':'"+email+"'}";
    }
}

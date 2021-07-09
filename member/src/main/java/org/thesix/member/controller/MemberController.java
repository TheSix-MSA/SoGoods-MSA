package org.thesix.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.RequestListDTO;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.service.MemberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     *회원가입
     * @param dto 회원가입정보(멤버의 DTO)
     * @return String email 이메일
     */
    @PutMapping("/signup")
    public ResponseEntity<String> SignUp(@RequestBody MemberDTO dto){

        dto.addMemberRole(MemberRole.GENERAL);
        String email = memberService.regist(dto);

        return ResponseEntity.ok(email);
    }



    /**
     *
     * @param email 검색할 유저의 이메일
     * @return jsonUser 반환할 유저의 계정정보
     */
    @GetMapping("/getUser/{email}")
    public ResponseEntity<String> getUser(@PathVariable("email") String email){

        String jsonUser = memberService.readUser(email);
        return ResponseEntity.ok(jsonUser);
    }


    /**
     *
     * @param email 삭제할 유저의 이메일
     * @return 상태메세지
     */
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map> delete(@PathVariable("email") String email) {

        memberService.delete(email);
        Map<String, String> map = new HashMap<>();

        map.put("email", email);
        map.put("msg", "삭제가 완료되었습니다.");

        return ResponseEntity.ok(map);
    }

    @PutMapping("/update")
    public ResponseEntity<MemberDTO> modify(@RequestBody MemberDTO dto){

        return ResponseEntity.ok(memberService.modify(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<MemberDTO>> readList(RequestListDTO dto){

        return ResponseEntity.ok(memberService.readList(dto));
    }

}

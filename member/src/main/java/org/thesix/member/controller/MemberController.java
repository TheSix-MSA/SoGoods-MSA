package org.thesix.member.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.RequestListDTO;
import org.thesix.member.dto.ResponseListDTO;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.service.MemberService;
import static org.thesix.member.util.ApiUtil.ApiResult;
import static org.thesix.member.util.ApiUtil.success;

import java.util.HashMap;
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
    @PostMapping("/")
    public ApiResult<MemberDTO> SignUp(@RequestBody MemberDTO dto){
        dto.addMemberRole(MemberRole.GENERAL);
        MemberDTO resultDTO = memberService.register(dto);
        return success(resultDTO);
    }

    /**
     * 회원 조회
     * @param email 검색할 유저의 이메일
     * @return jsonUser 반환할 유저의 계정정보
     */
    @GetMapping("/{email}")
    public ApiResult<MemberDTO> getUser(@PathVariable("email") String email){

        MemberDTO dto = memberService.readUser(email);
        return success(dto);
    }


    /**
     * 회원아이디 삭제
     * @param email 삭제할 유저의 이메일
     * @return 상태메세지
     */
    @DeleteMapping("/{email}")
    public ApiResult<Map> delete(@PathVariable("email") String email) {

        memberService.delete(email);
        Map<String, String> map = new HashMap<>();

        map.put("email", email);
        map.put("msg", "삭제가 완료되었습니다.");

        return success(map);
    }

    /**
     * 회원정보변경
     * @param dto
     * @return
     */
    @PutMapping("/")
    public ApiResult<MemberDTO> modify(@RequestBody MemberDTO dto){
        return success(memberService.modify(dto));
    }


    /**
     * 회원목록 조회
     * @param dto page, size, 검색어, 검색타입
     * @return 회원List, PageMaker, 요청파라미터
     */
    @GetMapping("/list")
    public ApiResult<ResponseListDTO> readList(RequestListDTO dto){
        return success(memberService.readList(dto));
    }

    /**
     * 회원 권한 바꾸기
     *
     * @param email email
     * @return MemberDTO
     */
    @PostMapping("/change/{email}")
    public ApiResult<MemberDTO> changeRole(@PathVariable("email") String email){
        return success(memberService.changeRole(email));
    }

    /**
     * 회원 ban 정보 바꾸기
     *
     * @param email email
     * @return MemberDTO
     */
    @PostMapping("/ban/{email}")
    public ApiResult<MemberDTO> changeBanned(@PathVariable("email") String email){
        return success(memberService.changeBanned(email));
    }



}

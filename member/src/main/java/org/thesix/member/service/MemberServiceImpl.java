package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.PageMaker;
import org.thesix.member.dto.RequestListDTO;
import org.thesix.member.dto.ResponseListDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.repository.MemberRepository;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder encoder;

    /**
     * 회원가입
     *
     * @param dto
     * @return true: MemberDTO
     * @throws IllegalArgumentException 존재하는 Email
     */
    @Override
    public MemberDTO register(MemberDTO dto) {
        boolean result = memberRepository.findById(dto.getEmail()).isEmpty();
        if(result){
            dto.setPassword(encoder.encode(dto.getPassword()));
            Member member = memberRepository.save(memberDTOToEntity(dto));
            return entityToMeberDTO(member);
        }

        throw new IllegalArgumentException("이미 존재하는 Email 입니다.");
    }

    /**
     * 사용자의 정보를 가져온다.
     *
     * @param email
     * @return MemberDTO
     */
    @Override
    public MemberDTO readUser(String email) {

        Member member = memberRepository.findById(email).orElseThrow(() -> new NullPointerException("해당하는 사용자가 없습니다."));
        MemberDTO dto = entityToMeberDTO(member);

        return dto;
    }

    /**
     * 사용자의 삭제정보를 true로 만든다.
     *
     * @param email
     * @return String email
     */
    @Override
    public String delete(String email) {
        Member member = memberRepository.findById(email).orElseThrow(()->new NullPointerException("해당하는 사용자가 없습니다."));

        member.changeRemoved(!member.isRemoved());

        memberRepository.save(member);

        return email;
    }

    /**
     * 사용자 정보 수정
     *
     * @param dto 새로 받은 사용자의 정보
     * @return MemberDTO 바뀐 사용자의 정보
     */
    @Override
    public MemberDTO modify(MemberDTO dto) {

        Member member = memberRepository.findById(dto.getEmail()).orElseThrow(() -> new NullPointerException("해당하는 사용자가 없습니다."));

        if(dto.getPassword() != null) {
            dto.setPassword(encoder.encode(dto.getPassword()));
            member.changePassword(memberDTOToEntity(dto));
        }else{
            dto.setPassword(member.getPassword());
            member.changeMemberInfo(memberDTOToEntity(dto));
        }

        Member inputResult = memberRepository.save(member);

        return entityToMeberDTO(inputResult);
    }

    /**
     * 멤버의 목록을 출력.
     *
     *
     * @param dto
     * @return ResponseListDTO (요청한 페이지 구성요소, 요청에 응답하는 리스트들, 페이지정보)
     */
    @Override
    public ResponseListDTO readList(RequestListDTO dto) {

        Pageable pageable = PageRequest.of(dto.getPage()-1, dto.getSize());

        Page<Object> memberList = memberRepository.getMemberList(dto.getType(), dto.getKeyword(), pageable, dto.isApproval());

        List<Object> memberListResult = memberList.toList();

        PageMaker pageMaker = new PageMaker(pageable, dto, (int) memberList.getTotalElements());


        return   ResponseListDTO.builder()
                .requestListDTO(dto)
                .memberList(memberListResult)
                .pageMaker(pageMaker)
                .build();
    }

    /**
     * 회원권한 변환
     * toggle로 Author와 General권한을 바꿀 수 있다.
     *
     * @param email
     * @return MemberDTO
     */
    @Override
    public MemberDTO changeRole(String email) {

        Member memberResult = memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("사용자가 없습니다."));

        Set<MemberRole> roleSet = memberResult.getRoleSet();

        if (roleSet.contains(MemberRole.AUTHOR)) {
            roleSet.remove(MemberRole.AUTHOR);
        } else {
            roleSet.add(MemberRole.AUTHOR);
        }
        memberResult.changeApproval(false);

        return entityToMeberDTO(memberRepository.save(memberResult));
    }

    /**
     * 회원의 ban 여부 변경
     *
     * @param email
     * @return MemberDTO
     */
    @Override
    public MemberDTO changeBanned(String email) {

        Member memberResult = memberRepository.findByEmail(email).orElseThrow(() -> new NullPointerException("사용자가 존재하지 않습니다."));

        boolean banned = memberResult.isBanned();

        if (banned) {
            memberResult.changeBanned(false);
        } else {
            memberResult.changeBanned(true);
        }

        return entityToMeberDTO(memberRepository.save(memberResult));
    }

}

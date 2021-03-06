package org.thesix.member.service;

import org.thesix.member.dto.AnalysisDTO;
import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.RequestListDTO;
import org.thesix.member.dto.ResponseListDTO;
import org.thesix.member.entity.Member;


public interface MemberService {



    // 회원가입등록
    MemberDTO register(MemberDTO dto);

    MemberDTO readUser(String email);

    String delete(String email);

    MemberDTO modify(MemberDTO dto);

    ResponseListDTO readList(RequestListDTO dto);

    MemberDTO changeRole(String email);

    MemberDTO changeBanned(String email);

    MemberDTO rejectRequest(String email);

    AnalysisDTO countUser();

    /**
     *
     * @param dto 가입입력정보
     * @return 멤버의 엔티티
     */
    default Member memberDTOToEntity(MemberDTO dto){
        Member member = Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .removed(dto.isRemoved())
                .banned(dto.isBanned())
                .provider(dto.getProvider())
                .social(dto.isSocial())
                .roleSet(dto.getRoleSet())
                .approval(dto.isApproval())
                .identificationUrl(dto.getIdentificationUrl())
                .introduce(dto.getIntroduce())
                .nickName(dto.getNickName())
                .build();

        return member;
    }

    /**
     * 엔티티를 MemberDTO로 변환
     * @param member
     * @return MemberDTO
     */
    default MemberDTO entityToMeberDTO(Member member) {
        return MemberDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .gender(member.getGender())
                .birth(member.getBirth())
                .phone(member.getPhone())
                .address(member.getAddress())
                .detailAddress(member.getDetailAddress())
                .removed(member.isRemoved())
                .banned(member.isBanned())
                .provider(member.getProvider())
                .social(member.isSocial())
                .regDate(member.getRegDate())
                .modDate(member.getModDate())
                .roleSet(member.getRoleSet())
                .identificationUrl(member.getIdentificationUrl())
                .introduce(member.getIntroduce())
                .nickName(member.getNickName())
                .approval(member.isApproval())
                .build();
    }


}

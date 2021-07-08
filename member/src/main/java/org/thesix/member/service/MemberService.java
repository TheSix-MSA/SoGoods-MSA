package org.thesix.member.service;

import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.RefreshDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.RefreshToken;

public interface MemberService {
    // 회원가입등록
    String regist(MemberDTO dto);

    String readUser(String email);

    /**
     *
     * @param dto 가입입력정보
     * @return 멤버의 엔티티
     */
    default Member memberDTOToEntity(MemberDTO dto){
        Member member = Member.builder()
                .email(dto.getEmail())
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
                .regDate(dto.getRegDate())
                .loginDate(dto.getLoginDate())
                .roleSet(dto.getRoleSet())
                .build();

        return member;
    }

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
                .loginDate(member.getLoginDate())
                .roleSet(member.getRoleSet())
                .build();
    }
}

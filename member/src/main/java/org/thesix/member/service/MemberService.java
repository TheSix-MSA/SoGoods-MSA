package org.thesix.member.service;

import org.thesix.member.dto.MemberDTO;
import org.thesix.member.dto.RefreshDTO;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.RefreshToken;

public interface MemberService {
    // 회원가입등록
    String regist(MemberDTO dto);

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
                .provider(dto.getPriveder())
                .social(dto.isSocial())
                .regDate(dto.getRegDate())
                .loginDate(dto.getLoginDate())
                .roleSet(dto.getRoleSet())
                .build();

        return member;
    }
}

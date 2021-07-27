package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.member.entity.MemberRole;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private String email;
    private String password;
    private String name;
    private String gender;
    private String birth;
    private String phone;
    private String address;
    private String detailAddress;
    private boolean removed;
    private boolean banned;
    private String provider;
    private boolean social;
    private boolean approval;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String identificationUrl; // 작가등록시 신분증 주소
    private String nickName; // 작가의 필명(혹은 본명)
    private String introduce; // 작가 본인소개 (50자제한)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole role) {
        roleSet.add(role);
    }


}

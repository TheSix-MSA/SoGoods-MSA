package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.member.entity.MemberRole;

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
    private LocalDateTime regDate;
    private LocalDateTime loginDate;
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();
    public void addMemberRole(MemberRole role) {
        roleSet.add(role);
    }


}

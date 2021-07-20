package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.member.entity.MemberRole;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

    private String accessToken;
    private String refreshToken;
    private String email;
    private Set<MemberRole> roles;
}

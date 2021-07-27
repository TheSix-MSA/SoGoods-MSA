package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.member.entity.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshDTO {

    private long rno;
    private Member member;
    private String refreshToken;
}

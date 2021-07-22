package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthorInfoDTO {
    private String identificationUrl;
    private String nickName;
    private String introduce;
}

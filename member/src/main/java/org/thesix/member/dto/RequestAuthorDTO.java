package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestAuthorDTO {
    NovelsDTO novelsDTO;
    AuthorInfoDTO authorInfoDTO;
}

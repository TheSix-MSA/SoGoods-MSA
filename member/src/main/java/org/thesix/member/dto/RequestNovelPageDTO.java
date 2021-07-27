package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestNovelPageDTO {

    private String email;
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 5;
}

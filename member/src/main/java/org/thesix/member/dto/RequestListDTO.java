package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestListDTO {

    @Builder.Default
    private int page=1;

    @Builder.Default
    private int size=10;

    private String keyword;

    private String type;

}

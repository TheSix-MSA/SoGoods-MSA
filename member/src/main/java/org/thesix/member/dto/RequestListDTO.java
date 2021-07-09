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
    private long page=1;

    private long size;

    private String keyword;

    private String type;

}

package org.thesix.member.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisDTO {
    private int total;
    private int author;
}

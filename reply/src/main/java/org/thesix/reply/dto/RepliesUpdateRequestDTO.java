package org.thesix.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepliesUpdateRequestDTO {
    private Long rno;
    private String email;
    private String content;
}

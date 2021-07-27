package org.thesix.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepliesSaveRequestDTO {
    private String writer;
    private String email;
    private String content;
    private boolean removed;
    private Long keyValue;
    @Builder.Default
    private Long groupId = 0L;
    private Long level;
    private Long parentId;


}

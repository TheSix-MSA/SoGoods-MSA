package org.thesix.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepliesResponseDTO {
    private Long rno;
    private String writer;
    private String email;
    private String content;
    private boolean removed;
    private Long groupId;
    private Long level;
    private Long parentId;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}

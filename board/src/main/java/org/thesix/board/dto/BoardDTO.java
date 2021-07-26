// 게시판 비닐봉지

package org.thesix.board.dto;

import lombok.*;
import org.thesix.board.entity.BoardType;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno; // PK
    private String title; // 게시글 제목
    private String writer; // 게시글 글쓴이
    private String email; // 게시글 글쓴이이메일
    private String content; // 게시글 내용
    private boolean removed; // 게시글 삭제여부
    private LocalDateTime regDate; // 게시글 작성일
    private LocalDateTime modDate; // 게시글 수정일
}

// 게시판 테이블
package org.thesix.board.entity;

import lombok.*;
import org.thesix.board.common.BoardDateEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_board")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BoardDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; // 게시글 식별번호(PK)

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(nullable = false)
    private String writer; // 게시글 작성자

    @Column(nullable = false)
    private String email; // 게시글 작성자이메일

    @Lob
    @Column(nullable = false)
    private String content; // 게시글 내용

    @Column(nullable = false)
    private boolean removed; // 게시글 삭제여부

    @Enumerated(EnumType.STRING)
    private BoardType boardType; // 게시글 종류

    // 제목수정
    public void changeTitle(String title) {
        this.title = title;
    }

    // 내용수정
    public void changeContent(String content) {
        this.content = content;
    }

    // 삭제유무
    public void changeRemoved(boolean removed) {
        this.removed = removed;
    }

}

// 게시판 테이블
package org.thesix.board.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    /*
        게시글 식별번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    /*
        게시글 제목
     */
    @Column(nullable = false)
    private String title;

    /*
        게시글 작성자
     */
    @Column(nullable = false)
    private String writer;

    /*
        게시글 작성자이메일
     */
    @Column(nullable = false)
    private String email;

    /*
        게시글 내용
     */
    @Lob
    @Column(nullable = false)
    private String content;

    /*
        게시글 삭제여부
     */
    @Column(nullable = false)
    private boolean removed;

    /*
        게시글 종류
     */
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    /*
        해당 글의 댓글개수
     */
    @Builder.Default
    @Column(nullable = false)
    private Long replyCnt=0L;

    /*
        공지글 공개&비공개 여부
     */
    @Column(nullable = false)
    private boolean isPrivate;

    /*
        제목수정
     */
    public void changeTitle(String title) {
        this.title = title;
    }

    /*
        내용수정
     */
    public void changeContent(String content) {
        this.content = content;
    }

    /*
        삭제유무
     */
    public void changeRemoved(boolean removed) {
        this.removed = removed;
    }

    /*
        공개/비공개유무
     */
    public void changeIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /*
        댓글 증가
     */
    public void replyCountUp(Long replyCnt) {
        this.replyCnt = replyCnt + 1L;
    }

    /*
        댓글 감소
     */
    public void replyCountDown(Long replyCnt) {
        this.replyCnt = replyCnt - 1L;
    }

}

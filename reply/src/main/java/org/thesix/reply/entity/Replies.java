package org.thesix.reply.entity;

import lombok.*;
import org.thesix.reply.entity.common.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Replies extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean removed;

    @Column(nullable = false)
    private Long keyValue;

    private Long groupId;

    @Column(nullable = false)
    private Long level;

    private Long parentId;

    public void changeGroupId(Long groupId){
        /**
         * 댓글 저장시 대댓글이 아닌 혼자 저장되야 할 경우 댓글을 저장하고 그 이후에 그룹 아이디를 업데이트 하는 방식으로
         * 저장하여야 하기에 만든 함수
         *
         */
        this.groupId=groupId;
    }

    public void updateReply(String content){
        /**
         * 댓글 수정시 content를 바꿔끼우는 함수
         */
        this.content = content;
    }

    public void deleteReply(){
        /***
         * 댓글의 상태를 deleted로 바꾸는 함수
         */
        this.removed = true;
    }
}

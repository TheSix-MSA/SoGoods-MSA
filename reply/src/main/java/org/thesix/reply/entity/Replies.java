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
        this.groupId=groupId;
    }
}

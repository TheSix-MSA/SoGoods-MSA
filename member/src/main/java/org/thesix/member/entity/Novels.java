package org.thesix.member.entity;

import lombok.*;
import org.thesix.member.common.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tbl_novels")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class Novels extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String title;

    private String image;

    @Column(nullable = false)
    private String publisher;

    @Builder.Default
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void changeDelete(){
        this.deleted = true;
    }

}

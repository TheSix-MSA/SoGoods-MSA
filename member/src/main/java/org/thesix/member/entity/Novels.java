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
@ToString
public class Novels extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nno;

    @Column(nullable = false)
    private String ISBN;

    @Column(nullable = false)
    private String title;

    private String Image;

    @Column(nullable = false)
    private String publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}

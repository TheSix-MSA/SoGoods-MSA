package org.thesix.funding.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "funding")
@Table(name = "tbl_favorite", indexes = @Index(name = "idx_funding", columnList = "funding_fno"))
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favno;

    private boolean mark;

    private String actor;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Funding funding;


}

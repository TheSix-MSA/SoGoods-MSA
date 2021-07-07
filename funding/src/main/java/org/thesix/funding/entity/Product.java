package org.thesix.funding.entity;

import lombok.*;
import org.thesix.funding.common.entity.BaseEntity;

import javax.persistence.*;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "funding")
@Table(name ="tbl_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno; // 제품 식별번호

    private String name;  // 제품 이름

    private String des;  // 제품 설명

    private int price;  // 제품 가격

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Funding funding;
}

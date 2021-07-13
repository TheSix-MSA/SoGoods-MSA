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
@Table(name = "tbl_product", indexes = @Index(name = "idx_funding", columnList = "funding_fno"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno; // 제품 식별번호

    private String name;  // 제품 이름

    private String des;  // 제품 설명

    private int price;  // 제품 가격

    private boolean removed;  // 삭제 여부

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Funding funding;

    public void changeName(String name){
        this.name = name;
    }

    public void changeDes(String des){
        this.des = des;
    }

    public void changePrice(int price){
        this.price = price;
    }

    public void changeRemoved(boolean removed){
        this.removed = removed;
    }
}

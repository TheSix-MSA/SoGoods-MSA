package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno; // 제품 식별번호

    private String name;  // 제품 이름

    private String des;  // 제품 설명

    private int price;  // 제품 가격

    private Long fno; // 펀딩 식별번호

    private boolean removed; // 삭제 여부
}

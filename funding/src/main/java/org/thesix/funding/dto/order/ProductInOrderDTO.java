package org.thesix.funding.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * 결제후 해당 결제내역의 상세정보용 상품 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInOrderDTO {
    private Long pno; // 제품 식별번호

    private String name;  // 제품 이름

    private String des;  // 제품 설명

    private int price;  // 제품 가격

    private Long amount; //주문한 갯수
}

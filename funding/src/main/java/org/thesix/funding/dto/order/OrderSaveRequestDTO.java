package org.thesix.funding.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSaveRequestDTO {
    private String buyer;

    private String receiverName;

    private String receiverAddress;

    private String receiverDetailedAddress;

    private String receiverPhone;

    private String receiverRequest; // 수령인의 요청사항 ex) 벨 누르지말고 그냥 두고 가라던지 등

    private String tid; // 계좌번호 등 결제 수단. 카카오 페이에선 TID라는 걸 제공하는 듯.

    private Map<Long, Long> products = new HashMap<>();
    /**
     * key ==> product id
     * value ==> number of products
     */
}

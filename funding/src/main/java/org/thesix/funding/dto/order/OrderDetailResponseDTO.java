package org.thesix.funding.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.dto.funding.FundingDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponseDTO {
    private Long ono;

    private String buyer;

    private String receiverName;

    private String receiverAddress;

    private String receiverDetailedAddress;

    private String receiverPhone;

    private String receiverRequest; // 수령인의 요청사항 ex) 벨 누르지말고 그냥 두고 가라던지 등

    private String tid; // 계좌번호 등 결제 수단. 카카오 페이에선 TID라는 걸 제공하는 듯.

    private LocalDateTime shippedDate;

    private LocalDateTime cancelledDate;

    private LocalDate regDate;

    private LocalDate modDate;

    private Set<ProductInOrderDTO> prods;

    private FundingDTO fundInfo;
}

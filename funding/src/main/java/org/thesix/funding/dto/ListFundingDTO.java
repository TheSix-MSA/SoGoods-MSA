package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListFundingDTO {

    private FundingDTO fundingDTO;  // 펀딩 글 객체

    private Long mainProductPno;  // 가장 먼저 저장된 제품 번호 -> 썸네일을 가져오기 위해 필요?

    private long favoriteCnt;  // 찜 여부

}

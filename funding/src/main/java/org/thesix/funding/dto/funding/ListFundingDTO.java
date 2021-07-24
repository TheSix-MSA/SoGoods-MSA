package org.thesix.funding.dto.funding;

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

    private long favoriteCnt;  // 찜 개수

}

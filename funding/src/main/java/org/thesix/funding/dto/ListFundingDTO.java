package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.entity.Favorite;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListFundingDTO {

    private FundingDTO fundingDTO;  // 펀딩 글 객체

    private ProductDTO productDTO;  // 제품 객체

    private Favorite favorite;  // 찜 여부

}

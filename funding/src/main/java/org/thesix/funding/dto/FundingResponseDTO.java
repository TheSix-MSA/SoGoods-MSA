package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingResponseDTO {

    private FundingDTO fundingDTO;  // 펀딩 글 객체

    private List<ProductDTO> productDTOs;  // 제품 리스트

    private long favoriteCount;  //  찜 개수

}

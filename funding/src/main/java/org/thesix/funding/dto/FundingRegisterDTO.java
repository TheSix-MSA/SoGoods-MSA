package org.thesix.funding.dto;

import lombok.Data;

import java.util.List;

@Data
public class FundingRegisterDTO extends FundingDTO{

    private List<ProductDTO> productDTOs;  // 추가된 제품 리스트

}

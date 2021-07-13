package org.thesix.funding.dto;

import lombok.Data;

@Data
public class FundingRegisterDTO extends FundingDTO{

    private ProductDTO[] productDTOs;  // 추가된 제품 배열

}

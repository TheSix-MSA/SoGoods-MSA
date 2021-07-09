package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.entity.Funding;

@Data
public class FundingRegisterDTO extends FundingDTO{

    private ProductDTO[] productDTOs;

}

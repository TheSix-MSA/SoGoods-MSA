package org.thesix.funding.dto;

import lombok.Data;

import java.util.List;

@Data
public class FundingModDTO extends FundingDTO{

    private List<ProductDTO> toBeDeletedDTO;

    private List<ProductDTO> toBeAddedDTO;



}

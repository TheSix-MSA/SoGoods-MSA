package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.dto.funding.FundingDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingRegResponseDTO {

    private FundingDTO fundingDTO;

    private List<Long> productNums;
}

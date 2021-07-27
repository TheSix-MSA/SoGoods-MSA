package org.thesix.funding.dto.funding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.dto.order.OrderResponseDTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingDeletionResponseDTO {
    private FundingResponseDTO fundingResponseDTO;
    private List<OrderResponseDTO> orderList;
    private List<Long> returnAmountList;
    private boolean success;
}

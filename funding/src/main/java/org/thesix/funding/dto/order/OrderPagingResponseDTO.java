package org.thesix.funding.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.common.dto.PageMaker;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPagingResponseDTO {
    private List<OrderBriefResponseDTO> resDto;
    private List<Integer> totalPrices;
    private List<String> prodsNames;
    private PageMaker pageMaker;
}

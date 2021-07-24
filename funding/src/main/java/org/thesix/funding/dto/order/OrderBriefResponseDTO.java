package org.thesix.funding.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderBriefResponseDTO {
    private OrderResponseDTO dto;
    private int totalPrices;
    private String prodNames;
}

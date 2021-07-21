package org.thesix.funding.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageRequestDTO {
    private int page;
    private String email;
    private String sortCondition;
}

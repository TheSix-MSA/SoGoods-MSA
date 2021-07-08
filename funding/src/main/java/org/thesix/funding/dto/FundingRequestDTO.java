package org.thesix.funding.dto;

import lombok.Data;
import org.thesix.funding.common.dto.ListRequestDTO;

@Data
public class FundingRequestDTO extends ListRequestDTO {

    private String type;  // 검색 타입

}

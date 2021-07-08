package org.thesix.funding.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponseDTO<D> {

    private ListRequestDTO listRequestDTO;

    private List<D> dtoList;

    private PageMaker pageMaker;

}

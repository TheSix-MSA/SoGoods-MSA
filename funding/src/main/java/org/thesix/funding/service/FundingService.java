package org.thesix.funding.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.FundingDTO;
import org.thesix.funding.dto.FundingRequestDTO;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.entity.Funding;

import java.util.List;

public interface FundingService {

    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto);

    default List<ListFundingDTO> arrToDTO(Object[] arr){

        Funding funding = (Funding) arr[0];

        return null;
    }

    default FundingDTO entityToDTO(Funding funding){
        return FundingDTO.builder()
                .fno(funding.getFno())
                .title(funding.getTitle())
                .content(funding.getContent())
                .email(funding.getEmail())
                .writer(funding.getWriter())
                .regDate(funding.getRegDate())
                .dueDate(funding.getDueDate())
                .removed(funding.isRemoved())
                .success(funding.isSuccess())
                .build();
    }

}

package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.FundingRequestDTO;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.repository.FundingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundingServiceImple implements FundingService {

    private final FundingRepository fundingRepository;

    /**
     *
     * @param dto
     * @return
     */
    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto){

        Pageable pageable = dto.getPageable();

        Page<Object[]> dtoList = fundingRepository.getListSearch(dto.getKeyword(), dto.getType(), pageable);

       // List<ListFundingDTO> dtoList;

        return null;
    }


}

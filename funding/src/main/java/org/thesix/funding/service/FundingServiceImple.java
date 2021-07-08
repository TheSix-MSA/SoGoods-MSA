package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.common.dto.PageMaker;
import org.thesix.funding.dto.FundingDTO;
import org.thesix.funding.dto.FundingRequestDTO;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.repository.FundingRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class FundingServiceImple implements FundingService {

    private final FundingRepository fundingRepository;

    /**
     * @param dto
     * @return
     */
    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto){

        Pageable pageable = dto.getPageable();

        Page<Object[]> result = fundingRepository.getListSearch(dto.getKeyword(), dto.getType(), pageable);

        // Function<T,R> : 객체 T를 R로 매핑
//        Function<Funding, FundingDTO> fn = (todo) -> entityToDTO(todo);
//
//        List<FundingDTO> dtoList = result.getContent().stream()
//                .map()
//                .collect(Collectors.toList());
//
//        PageMaker pageMaker = new PageMaker(dto.getPage(), dto.getSize(), (int) result.getTotalElements());
//
//        log.info(pageMaker);
//
//        ListResponseDTO<FundingDTO> listResult = ListResponseDTO.<FundingDTO>builder()
//                .dtoList(dtoList)
//                .pageMaker(pageMaker)
//                .listRequestDTO(dto).build();

 //       log.info(listResult);

        return null;
    }



}

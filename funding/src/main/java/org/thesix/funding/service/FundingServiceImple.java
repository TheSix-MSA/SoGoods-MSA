package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.common.dto.PageMaker;
import org.thesix.funding.dto.*;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;
import org.thesix.funding.repository.FundingRepository;
import org.thesix.funding.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
@RequiredArgsConstructor
public class FundingServiceImple implements FundingService {

    private final FundingRepository fundingRepository;

    private final ProductRepository productRepository;

    /**
     * @param dto
     * @return
     */
    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto){

        Pageable pageable = dto.getPageable();

        Page<Object[]> result = fundingRepository.getListSearch(dto.getKeyword(), dto.getType(), pageable);
        log.info(result.getContent());

        List<ListFundingDTO> dtoList = result.getContent().stream().map(arr -> arrToDTO(arr)).collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(dto.getPage(), dto.getSize(), (int) result.getTotalElements());

        return ListResponseDTO.<ListFundingDTO>builder().listRequestDTO(dto).dtoList(dtoList).pageMaker(pageMaker).build();
    }


     /**
     * 글 등록 처리를 위한 메서드 + 예외처리 해야함...!
     * @param FundingRegisterDTO
     * @return FundingDTO
     */
    @Override
    public FundingDTO register(FundingRegisterDTO registerDTO) {

            Funding funding = Funding.builder()
                    .title(registerDTO.getTitle())
                    .writer(registerDTO.getWriter())
                    .email(registerDTO.getEmail())
                    .content(registerDTO.getContent())
                    .dueDate(registerDTO.getDueDate())
                    .success(registerDTO.isSuccess())
                    .removed(registerDTO.isRemoved())
                    .build();

            fundingRepository.save(funding);

            ProductDTO[] productDTOS = registerDTO.getProductDTOs();
            log.info(productDTOS);

            for(ProductDTO dto : productDTOS ){

                Product product = Product.builder()
                        .name(dto.getName())
                        .price(dto.getPrice())
                        .des(dto.getDes())
                        .funding(funding).build();

                productRepository.save(product);
            }
            FundingDTO resultDTO = entityToDTO(funding);

            return resultDTO;
    }

    @Override
    public ListFundingDTO getData(Long fno) {

        Optional<Object[]> result1 = fundingRepository.getFundingById(2L);
        //Object result2 = productRepository.getProductById(2L);
        return null;
    }


}

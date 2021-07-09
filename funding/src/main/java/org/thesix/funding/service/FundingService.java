package org.thesix.funding.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.*;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;

import java.util.List;

public interface FundingService {

    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto);

    /**
     * 펀딩글 객체를 DTO로 변환하는 메서드
     * @param funding
     * @return FundingDTO
     */
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

    /**
     * 제품의 엔티티객체를 DTO로 변환하는 메서드
     * @param product
     * @return ProductDTO
     */
    default ProductDTO entityToDTO(Product product){

        Funding funding = Funding.builder().fno(product.getFunding().getFno()).build();

        return ProductDTO.builder()
                .pno(product.getPno())
                .name(product.getName())
                .price(product.getPrice())
                .des(product.getDes())
                .fno(funding.getFno()).build();
    }

    /**
     * 배열을 받아 ListFundingDTO객체로 변환하는 메서드
     * @param arr
     * @return ListFundingDTO
     */
    default ListFundingDTO arrToDTO(Object[] arr){

        Funding funding = (Funding) arr[0];
        long productCnt = (long) arr[1];
        long favoriteCnt = (long) arr[2];

        return ListFundingDTO.builder()
                .fundingDTO(entityToDTO(funding)).productCnt(productCnt).favoriteCnt(favoriteCnt).build();
    }

    /**
     * 글 등록 처리를 위한 추상메서드
     * @param fundingDTO
     * @return FundingDTO
     */
    public FundingDTO register(FundingRegisterDTO registerDTO);

    public ListFundingDTO getData(Long fno);


}

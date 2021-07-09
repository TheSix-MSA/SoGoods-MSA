package org.thesix.funding.service;

import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.*;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;

public interface FundingService {

    ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto);

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
     * FundingRegisterDTO를 Funding 엔티티로 변환하는 메서드
     * @param registerDTO
     * @return
     */
    default Funding dtoToEntity(FundingRegisterDTO registerDTO){
        return Funding.builder()
                .title(registerDTO.getTitle())
                .content(registerDTO.getContent())
                .email(registerDTO.getEmail())
                .writer(registerDTO.getWriter())
                .dueDate(registerDTO.getDueDate())
                .removed(registerDTO.isRemoved())
                .success(registerDTO.isSuccess())
                .totalAmount(registerDTO.getTotalAmount()).build();
    }

    /**
     * 글 등록 처리를 위한 추상메서드
     * @param registerDTO
     * @return FundingDTO
     */
    FundingDTO register(FundingRegisterDTO registerDTO);

    /**
     * 글 하나를 가져오는 추상메서드
     * @param fno
     * @return FundingResponseDTO
     */
    FundingResponseDTO getData(Long fno);

    /**
     * 글 수정처리를 위한 추상메서드
     * @param fno
     * @param registerDTO
     * @return FundingResponseDTO
     */
    FundingResponseDTO modify(Long fno, FundingRegisterDTO registerDTO);

    /**
     * 삭제여부를 변경하는 추상메서드
     * @param fno
     * @return FundingResponseDTO
     */
    FundingResponseDTO remove(Long fno);

}

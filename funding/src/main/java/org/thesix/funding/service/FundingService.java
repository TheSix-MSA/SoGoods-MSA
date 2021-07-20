package org.thesix.funding.service;

import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.*;
import org.thesix.funding.entity.Favorite;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;

import java.util.Arrays;
import java.util.List;


public interface FundingService {

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
                .totalAmount(funding.getTotalAmount())
                .targetAmount(funding.getTargetAmount())
                .authorized(funding.isAuthorized())
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

    default Product dtoToEntity(ProductDTO dto, Funding funding){
        return  Product.builder()
                .pno(dto.getPno())
                .name(dto.getName())
                .des(dto.getDes())
                .price(dto.getPrice())
                .funding(funding).build();
    }


    /**
     * 배열을 받아 ListFundingDTO객체로 변환하는 메서드
     * @param arr
     * @return ListFundingDTO
     */
    default ListFundingDTO arrToDTO(Object[] arr){

        Funding funding = (Funding) arr[0];
        Long mainProductPno = (Long) arr[1];
        long favoriteCnt = (long) arr[2];

        return ListFundingDTO.builder()
                .fundingDTO(entityToDTO(funding)).mainProductPno(mainProductPno).favoriteCnt(favoriteCnt).build();
    }

    default FundingDTO arrToEntity(Object[] resultArr){
        Funding funding = (Funding) resultArr[1];
        FundingDTO dto = entityToDTO(funding);
       return dto;
    }


    /**
     * FundingRegisterDTO를 Funding 엔티티로 변환하는 메서드
     * @param modDTO
     * @return
     */
    default Funding dtoToEntity(FundingModDTO modDTO){

        return Funding.builder()
                .title(modDTO.getTitle())
                .content(modDTO.getContent())
                .email(modDTO.getEmail())
                .writer(modDTO.getWriter())
                .dueDate(modDTO.getDueDate())
                .removed(modDTO.isRemoved())
                .success(modDTO.isSuccess())
                .totalAmount(modDTO.getTotalAmount())
                .targetAmount(modDTO.getTargetAmount())
                .authorized(modDTO.isAuthorized())
                .build();
    }

    default Funding dtoToEntity(FundingRegisterDTO registerDTO){

        return Funding.builder()
                .title(registerDTO.getTitle())
                .content(registerDTO.getContent())
                .email(registerDTO.getEmail())
                .writer(registerDTO.getWriter())
                .dueDate(registerDTO.getDueDate())
                .removed(registerDTO.isRemoved())
                .success(registerDTO.isSuccess())
                .totalAmount(registerDTO.getTotalAmount())
                .targetAmount(registerDTO.getTargetAmount())
                .authorized(registerDTO.isAuthorized())
                .build();
    }

    /**
     * Favorite엔티티를 FavoriteDTO로 변환하는 메서드
     * @param favorite
     * @return FavoriteDTO
     */
    default FavoriteDTO entityToDTO(Favorite favorite){

        Funding funding = Funding.builder().fno(favorite.getFunding().getFno()).build();

        return FavoriteDTO.builder()
                .favno(favorite.getFavno())
                .actor(favorite.getActor())
                .funFno(funding.getFno()).build();
    }

    /**
     * 글 리스트를 가져올 추상메서드
     * @param dto
     * @return ListResponseDTO<ListFundingDTO>
     */
    ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto);

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
    FundingResponseDTO getDetailFundingData(Long fno);

    /**
     * 글 수정처리를 위한 추상메서드
     * @param fno
     * @param modDTO
     * @return FundingResponseDTO
     */
    FundingResponseDTO modify(Long fno, FundingModDTO modDTO);

    /**
     * 삭제여부를 변경하는 추상메서드
     * @param fno
     * @return FundingResponseDTO
     */
    FundingResponseDTO remove(Long fno);

    /**
     * 찜하기 기능을 위한 추상메서드
     * @param fno, email
     * @return FavoriteDTO
     */
    Long insertFavorite(Long fno, String email);


    /**
     * 찜한 게시글을 가져올 추상메서드
     * @param email
     * @return List<FundingDTO>
     */
    List<FundingDTO> getFavoriteFunding(String email);

    /**
     * 유저가 작성한 게시글을 가져올 추상메서드
     * @param email
     * @return List<FundingDTO>
     */
    List<FundingDTO> getFundingList(String email);

}

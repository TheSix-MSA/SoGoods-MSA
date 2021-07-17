package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.common.dto.PageMaker;
import org.thesix.funding.dto.*;
import org.thesix.funding.entity.Favorite;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;
import org.thesix.funding.repository.FavoriteRepository;
import org.thesix.funding.repository.FundingRepository;
import org.thesix.funding.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class FundingServiceImpl implements FundingService {

    private final FundingRepository fundingRepository;
    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    /**
     * 검색 + 페이징 + 전체리스트
     * @param dto
     * @return
     */
    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto) {

        Pageable pageable = dto.getPageable();

        Page<Object[]> result = fundingRepository.getListSearch(dto.getKeyword(), dto.getType(), pageable);

        List<ListFundingDTO> dtoList = result.getContent().stream().map(arr -> arrToDTO(arr)).collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(dto.getPage(), dto.getSize(), (int) result.getTotalElements());

        return ListResponseDTO.<ListFundingDTO>builder().listRequestDTO(dto).dtoList(dtoList).pageMaker(pageMaker).build();
    }


    /**
     * 글 등록 처리를 위한 메서드 + 예외처리 해야함...!
     * @param registerDTO
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
                .totalAmount(registerDTO.getTotalAmount())
                .targetAmount(registerDTO.getTargetAmount())
                .build();

        fundingRepository.save(funding);

        ProductDTO[] productDTOS = registerDTO.getProductDTOs();

        for (ProductDTO dto : productDTOS) {

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


    /**
     * 글 상세페이지에 보여줄 데이터를 가져오는 서비스 메서드
     * @param fno
     * @return FundingResponseDTO
     */
    @Override
    public FundingResponseDTO getData(Long fno) {

        Optional<Funding> funding = fundingRepository.getFundingById(fno);
        Optional<Product[]> products1 = productRepository.getProductById(fno);
        Optional<Long> favoriteCount = favoriteRepository.getFavoriteCntById(fno);

        FundingDTO fundingDTO = entityToDTO(funding.get());

        FundingResponseDTO responseDTO = FundingResponseDTO.builder()
                .fundingDTO(fundingDTO).productDTOs(Arrays.stream(products1.get())
                        .map(product -> entityToDTO(product)).collect(Collectors.toList()))
                .favoriteCount(favoriteCount.get())
                .build();

        return responseDTO;
    }


    /**
     * 글 수정 처리를 위한 메서드
     * @param fno
     * @param registerDTO
     * @return FundingResponseDTO
     */
    @Override
    public FundingResponseDTO modify(Long fno, FundingRegisterDTO registerDTO) {
        // FundingDTO -> Entity
        Funding fundingEntity = dtoToEntity(registerDTO);

        Optional<Funding> funding = fundingRepository.getFundingById(fno);
        // 글 내용 수정
        funding.get().changeContent(fundingEntity.getContent());
        funding.get().changeTitle(fundingEntity.getTitle());
        funding.get().changeDueDate(fundingEntity.getDueDate());
        funding.get().changeTotalAmount(fundingEntity.getTotalAmount());

        fundingRepository.save(funding.get());

        for (ProductDTO dto : registerDTO.getProductDTOs()) {
            // ProductDTO -> Entity
            Product productEntity = Product.builder()
                    .pno(dto.getPno())
                    .name(dto.getName())
                    .des(dto.getDes())
                    .price(dto.getPrice())
                    .funding(funding.get()).build();

            Optional<Product[]> products = productRepository.getProductById(fno);

            for (Product p : products.get()) {
                p.changeName(productEntity.getName());
                p.changeDes(productEntity.getDes());
                p.changePrice(productEntity.getPrice());

                productRepository.save(p);

            }
        }
        // 리턴값 출력을 위해 Entity -> DTO 변환
        FundingDTO dto = entityToDTO(funding.get());
        Optional<Product[]> products = productRepository.getProductById(fno);

        return FundingResponseDTO.builder().fundingDTO(dto)
                .productDTOs(Arrays.stream(products.get())
                        .map(product -> entityToDTO(product)).collect(Collectors.toList())).build();
    }

    /**
     * 삭제 여부를 변경하는 메서드
     * @param fno
     * @return FundingResponseDTO
     */
    @Override
    public FundingResponseDTO remove(Long fno) {

        Optional<Funding> fundingResult = fundingRepository.findById(fno);

        if (fundingResult.get().isRemoved() == false) {
            fundingResult.get().changeRemoved(true);
            fundingRepository.save(fundingResult.get());
        }

        Optional<Product[]> products = productRepository.getProductById(fno);

        for (Product p : products.get()) {
            if (p.isRemoved() == false) {
                p.changeRemoved(true);
                productRepository.save(p);
            }
        }

        Optional<Funding> funding = fundingRepository.findById(fno);
        Optional<Product[]> productList = productRepository.getProductById(fno);
        FundingDTO dto = entityToDTO(funding.get());

        return FundingResponseDTO.builder()
                .fundingDTO(dto)
                .productDTOs(Arrays.stream(productList.get())
                        .map(product1 -> entityToDTO(product1)).collect(Collectors.toList())).build();
    }

    /**
     * 찜하기 기능 처리
     * @param favoriteDTO
     * @return FavoriteDTO
     */
    @Override
    public Long insertFavorite(FavoriteDTO favoriteDTO){

        Funding funding = Funding.builder()
                .fno(favoriteDTO.getFunFno()).build();

        Favorite favorite = Favorite.builder()
                .mark(true)
                .actor(favoriteDTO.getActor())
                .funding(funding)
                .build();

        favoriteRepository.save(favorite);

        return favoriteRepository.getFavoriteCntById(favoriteDTO.getFunFno()).get();
    }

    @Override
    public FundingDTO[] getFavoriteFunding(String email) {

        Funding[] fundings = fundingRepository.getFundingByemail(email);

        for(Funding f : fundings){
            FundingDTO result = entityToDTO(f);
        }

        return null;
    }

}


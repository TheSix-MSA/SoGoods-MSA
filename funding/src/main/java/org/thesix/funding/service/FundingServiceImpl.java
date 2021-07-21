package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.common.dto.PageMaker;
import org.thesix.funding.dto.funding.*;
import org.thesix.funding.entity.Favorite;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Product;
import org.thesix.funding.repository.FavoriteRepository;
import org.thesix.funding.repository.FundingRepository;
import org.thesix.funding.repository.ProductRepository;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
     * 삭제여부 false, 인증여부 true만 통과
     * @param dto
     * @return ListResponseDTO<ListFundingDTO>
     */
    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto) {

        Pageable pageable = dto.getPageable();

        Page<Object[]> result = fundingRepository.getListSearch(dto.getKeyword(), dto.getType(), pageable);

        List<ListFundingDTO> dtoList = result.getContent().stream().map(arr -> arrToDTO(arr)).collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(dto.getPage(), dto.getSize(), (int) result.getTotalElements());

        return ListResponseDTO.<ListFundingDTO>builder().listRequestDTO(dto).dtoList(dtoList).pageMaker(pageMaker).build();
    }


    /**
     * 글 등록 처리를 위한 메서드
     * @param registerDTO
     * @return FundingDTO
     */
    @Transactional
    @Override
    public FundingDTO register(FundingRegisterDTO registerDTO) {

        // FundingDTO -> Entity
        Funding funding = dtoToEntity(registerDTO);

        fundingRepository.save(funding);

        List<ProductDTO> productDTOS = registerDTO.getProductDTOs();

        for (ProductDTO dto : productDTOS) {

            Product product = Product.builder()
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .des(dto.getDes())
                    .funding(funding).build();

            productRepository.save(product);
        }
        return entityToDTO(funding);
    }


    /**
     * 글 상세페이지에 보여줄 데이터를 가져오는 서비스 메서드
     * 글이 존재하며 승인되었고, 제품중에 삭제되지 않은 데이터 출력
     * @param fno
     * @return FundingResponseDTO
     */
    @Override
    public FundingResponseDTO getDetailFundingData(Long fno){
        List<Object[]> result = fundingRepository.getFundingData(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));
        List<Object> res = new ArrayList<>();
        List<ProductDTO> proList = result.stream().map(obj -> entityToDTO((Product)obj[0])).collect(Collectors.toList());
        res.add(proList);
        res.add(result.get(0)[1]);
        res.add(result.get(0)[2]);

        return FundingResponseDTO.builder()
                .fundingDTO(arrToEntity(result.get(1)))
                .productDTOs(proList)
                .favoriteCount((long)res.get(2)).build();
    }


    /**
     * 글 수정 처리를 위한 메서드
     * 제목, 내용, 현재 토탈모금액 수정가능, 제품은 개수 추가 및 삭제가능(이미 구매가 이루어진 경우 불가).
     * @param fno
     * @param modDTO
     * @return FundingResponseDTO
     */
    @Override
    @Transactional
    public FundingResponseDTO modify(Long fno, FundingModDTO modDTO) {

        // FundingDTO -> Entity
        Funding fundingEntity = dtoToEntity(modDTO);

        // 해당 게시글이 존재하고, 인증이 됐는지 체크 후 예외처리
        Funding funding = fundingRepository.getFundingById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        // 글 내용 수정
        funding.changeTitle(fundingEntity.getTitle());
        funding.changeContent(fundingEntity.getContent());
        funding.changeTotalAmount(fundingEntity.getTotalAmount());

        fundingRepository.save(funding);

        // 삭제할 제품 리스트
        if(modDTO.getToBeDeletedDTO() !=null) {
            for (ProductDTO p1 : modDTO.getToBeDeletedDTO()) {
                // ProductDTO -> Entity
                Product product = dtoToEntity(p1, funding);
                // 제품번호를 받아서 해당 제품 removed 처리
                Product deletedProduct = productRepository.getOneProduct(product.getPno())
                        .orElseThrow(() -> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));
                product.changeRemoved(true);
                productRepository.save(product);
            }
        }

        // 추가할 제품 리스트
        if(modDTO.getToBeAddedDTO() != null) {
            for (ProductDTO dto : modDTO.getToBeAddedDTO()) {
                // ProductDTO -> Entity
                Product product = dtoToEntity(dto, funding);
                productRepository.save(product);
            }
        }

        // 리턴값 출력을 위해 Entity -> DTO 변환
        FundingDTO dto = entityToDTO(funding);
        Optional<List<Product>> productList = productRepository.getProductById(fno);

        return FundingResponseDTO.builder().fundingDTO(dto)
                .productDTOs(productList.get().stream().map(product -> entityToDTO(product))
                        .collect(Collectors.toList())).build();
    }

    /**
     * 삭제 여부를 변경하는 메서드
     * @param fno
     * @return FundingResponseDTO
     */
    @Override
    @Transactional
    public FundingResponseDTO remove(Long fno) {

        Funding fundingResult = fundingRepository.findById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        if (fundingResult.isRemoved() == false) {
            // 게시글의 삭제 정보를 true로 변경
            fundingResult.changeRemoved(true);
            fundingRepository.save(fundingResult);
        } else {
            // 게시물이 이미 삭제된 경우 예외처리
            throw new NullPointerException("이미 삭제된 게시물입니다.");
        }

        List<Product> products = productRepository.getProductById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        for (Product p : products) {
            if (p.isRemoved() == false) {
                // 해당 게시글의 제품 리스트 삭제 정보를 true로 변경
                p.changeRemoved(true);
                productRepository.save(p);
            }
        }

        // 삭제 완료 후 해당 게시글의 데이터 반환 (removed처리 확인)
        Funding funding = fundingRepository.findById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        List<Product> productList = productRepository.getProductById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        FundingDTO dto = entityToDTO(funding);

        return FundingResponseDTO.builder()
                .fundingDTO(dto)
                .productDTOs(productList.stream().map(list-> entityToDTO(list))
                        .collect(Collectors.toList())).build();
    }

    /**
     * 펀딩 글 찜하기 기능
     * @param favoriteDTO
     * @return FavoriteDTO
     */
    @Override
    public Long insertFavorite(FavoriteDTO favoriteDTO){

        Funding funding = Funding.builder()
                .fno(favoriteDTO.getFunFno()).build();

        // 찜한 주체, 게시글 번호를 받아 해당 유저 정보가 있는지 확인
        Favorite checkFavorite = favoriteRepository.checkUser(favoriteDTO.getActor(), favoriteDTO.getFunFno())
                        .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        if(checkFavorite.isMark()){
            // 유저 정보가 존재하고,
            // 이미 찜한 게시글일 경우 mark -> false
            checkFavorite.changeMark(!favoriteDTO.isMark());
            favoriteRepository.save(checkFavorite);

        } else {
            // 유저 정보가 존재하지 않고,
            // 첫 찜일 경우 insert 실행
            Favorite favorite = Favorite.builder()
                    .mark(true)
                    .actor(favoriteDTO.getActor())
                    .funding(funding)
                    .build();

            favoriteRepository.save(favorite);
        }
        return favoriteRepository.getFavoriteCntById(favoriteDTO.getFunFno())
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));
    }

    /**
     * 이메일 정보를 받아 해당 유저가 찜한 펀딩 리스트를 가져오는 기능
     * @param email
     * @return List<FundingDTO>
     */
    @Override
    public List<FundingDTO> getFavoriteFunding(String email) {

        List<Funding> fundings = fundingRepository.getFavoriteFundingByEmail(email)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        return fundings.stream().map(funding -> entityToDTO(funding)).collect(Collectors.toList());
    }

    /**
     * 이메일을 정보를 받아 해당 유저가 작성한 게시물 리스트를 가져오는 기능
     * @param email
     * @return List<FundingDTO>
     */
    @Override
    public List<FundingDTO> getFundingList(String email) {

        List<Funding> fundings = fundingRepository.getFundingListByEmail(email)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        return fundings.stream().map(funding -> entityToDTO(funding)).collect(Collectors.toList());
    }


}


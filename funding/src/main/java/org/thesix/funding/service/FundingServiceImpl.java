package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListRequestDTO;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.common.dto.PageMaker;
import org.thesix.funding.dto.FavoriteRequestDTO;
import org.thesix.funding.dto.FavoriteResponseDTO;
import org.thesix.funding.dto.FundingRegResponseDTO;
import org.thesix.funding.dto.funding.*;
import org.thesix.funding.dto.order.OrderResponseDTO;
import org.thesix.funding.entity.*;
import org.thesix.funding.repository.FavoriteRepository;
import org.thesix.funding.repository.FundingRepository;
import org.thesix.funding.repository.OrderRepository;
import org.thesix.funding.repository.ProductRepository;
import javax.transaction.Transactional;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final OrderRepository orderRepository;

    /**
     * 펀딩리스트에서 검색 + 페이징을 처리
     * 삭제여부 false, 인증여부 true만 통과
     * 마감일자가 되면 자동으로 펀딩종료 처리
     * @param dto
     * @return ListResponseDTO<ListFundingDTO>
     */
    public ListResponseDTO<ListFundingDTO> getSearchList(FundingRequestDTO dto) {

        Pageable pageable = dto.getPageable();

        List<Funding> fundings = fundingRepository.findAll();

        // 리스트 조회 시 마감일자가 오늘일 경우 success = true 처리
        Date from = new Date();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String to = transFormat.format(from);

        for(Funding f : fundings){
            if(f.getDueDate().equals(to)){
                f.changeSuccessed(true);
                fundingRepository.save(f);
            }
        }

        Page<Object[]> result = fundingRepository.getListSearch(dto.getKeyword(), dto.getType(), dto.getState(), pageable);

        List<ListFundingDTO> dtoList = result.getContent().stream().map(arr -> arrToDTO(arr)).collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(dto.getPage(), dto.getSize(), (int) result.getTotalElements());

        return ListResponseDTO.<ListFundingDTO>builder().listRequestDTO(dto).dtoList(dtoList).pageMaker(pageMaker).build();
    }


    /**
     * 글 등록 처리를 위한 메서드
     * @param registerDTO
     * @return FundingRegResponseDTO
     */
    @Transactional
    @Override
    public FundingRegResponseDTO register(FundingRegisterDTO registerDTO) {

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


        List<Product> products = productRepository.getPnoList(funding.getFno())
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));

        List<Long> num = new ArrayList<>();

        for(Product p : products){
            num.add(p.getPno());
        }

        return FundingRegResponseDTO.builder().fundingDTO(entityToDTO(funding)).productNums(num).build();
    }


    /**
     * 글 상세페이지에 보여줄 데이터를 가져오는 서비스 메서드
     * 글이 존재하며 승인되었고, 제품중에 삭제되지 않은 데이터 출력
     * @param fno
     * @return FundingResponseDTO
     */
    @Override
    public FundingResponseDTO getDetailFundingData(Long fno){
        List<Object[]> result = fundingRepository.getFundingALLData(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));
        log.info(result.get(0)[0]);
        log.info(result.get(0)[1]);
        List<Object> res = new ArrayList<>();
        List<ProductDTO> proList = result.stream().map(obj -> entityToDTO((Product)obj[0])).collect(Collectors.toList());
        res.add(proList);
        res.add(result.get(0)[1]);

        Long favCnt = favoriteRepository.getFavoriteCntById(fno)
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));

        return FundingResponseDTO.builder()
                .fundingDTO(arrToEntity(result.get(0)))
                .productDTOs(proList)
                .favoriteCount(favCnt).build();
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
     *
     * 07/25 종현 : 아직 마감하지 않은 펀딩이 작가에 의해 삭제될 때, 해당 펀딩에
     * 주문들을 전부 결제 취소할수 있게 값을 리턴.
     * 게시글 removed처리 -> 관련 제품 removed처리 -> 게시글 관련 찜 delete
     * @param fno
     * @return FundingResponseDTO
     */
    @Override
    public FundingDeletionResponseDTO remove(Long fno) {
        Funding fundingResult = fundingRepository.findById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        if (!fundingResult.isRemoved()) {
            // 게시글의 삭제 정보를 true로 변경
            fundingResult.changeRemoved(true);
            fundingRepository.save(fundingResult);
        } else {
            // 게시물이 이미 삭제된 경우 예외처리
            throw new NullPointerException("이미 삭제된 게시물입니다.");
        }

        List<Product> products = productRepository.getProductById(fno)
                .orElseThrow(()-> new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

        /***
         * simultaneous Order cancellation when funding is deleted.
         */
        List<OrderResponseDTO> orderList = new ArrayList<>();

        Map<Long, Long> priceMap = new HashMap<>();

        long totalPrice = 0L;
        List<Object[]> orderInfoFromProd = null;

        for (Product p : products) {
            if (!p.isRemoved()) {
                // 해당 게시글의 제품 리스트 삭제 정보를 true로 변경
                p.changeRemoved(true);
                productRepository.save(p);
            }
            if(!fundingResult.isSuccess()){
                orderInfoFromProd = orderRepository.getOrderInfoFromProduct(p);
                for(Object[] obj: orderInfoFromProd){
                    if(obj[0]!=null){
                        totalPrice += p.getPrice()*((Long)obj[1]);

                        if(!orderList.contains(orderToDto((Order)obj[0]))){
                            orderList.add(orderToDto((Order)obj[0]));
                        }

                        if(priceMap.containsKey(((Order)obj[0]).getOno())){
                            priceMap.put(((Order)obj[0]).getOno(),
                                    priceMap.get(((Order)obj[0]).getOno())+totalPrice);
                        } else {
                            priceMap.put(((Order)obj[0]).getOno(), totalPrice);
                        }
                    }
                    totalPrice = 0L;
                }
            }
        }

        // 해당 게시글과 관련된 찜 객체 모두 삭제
        List<Favorite> favorites = favoriteRepository.getFavorite(fno)
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));

        for(Favorite f : favorites){
            favoriteRepository.deleteById(f.getFavno());
        }

        List<Long> priceLists = new ArrayList<>(priceMap.values());

        FundingDTO dto = entityToDTO(fundingResult);

        return FundingDeletionResponseDTO.builder()
                .fundingResponseDTO(FundingResponseDTO.builder()
                        .fundingDTO(dto)
                        .productDTOs(products.stream().map(list-> entityToDTO(list))
                                .collect(Collectors.toList()))
                        .build())
                .orderList(orderList)
                .returnAmountList(priceLists)
                .success(fundingResult.isSuccess()).build();
    }

    /**
     * 해당 펀딩 글의 찜 리스트를 불러오는 기능
     * @param fno
     * @return FavoriteResponseDTO
     */
    @Override
    public FavoriteResponseDTO getFavoriteList(Long fno) {

        List<Favorite> list = favoriteRepository.getFavorite(fno)
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));

        log.info(list);

        Long favCnt = favoriteRepository.getFavoriteCntById(fno)
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));

        log.info(favCnt);

        return FavoriteResponseDTO.builder()
                .favoriteDTOList(list.stream().map(f-> entityToDTO(f)).collect(Collectors.toList()))
                .favoriteCnt(favCnt).build();
    }

    /**
     * 펀딩 글 찜하기 기능
     * 유저 정보를 확인하여 insert
     * @param dto
     * @return FavoriteResponseDTO
     */
    @Override
    public FavoriteResponseDTO insertFavorite(FavoriteRequestDTO dto){

        Funding funding = Funding.builder()
                .fno(dto.getFno()).build();

        // 찜한 주체, 게시글 번호를 받아 해당 유저 정보가 있는지 확인
        Optional<Favorite> checkFavorite = favoriteRepository.checkUser(dto.getEmail(), dto.getFno());

        if(checkFavorite.isPresent()){
            // 유저 정보가 존재하고,
            // 이미 찜한 게시글일 경우 mark -> false
            favoriteRepository.deleteById(checkFavorite.get().getFavno());

        } else{
            // 유저 정보가 존재하지 않고,
            // 첫 찜일 경우 insert 실행
            Favorite favorite = Favorite.builder()
                    .actor(dto.getEmail())
                    .funding(funding)
                    .build();
            favoriteRepository.save(favorite);
        }

        // 반환할 결과
        List<Favorite> list = favoriteRepository.getFavorite(dto.getFno())
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));
        Long favCnt = favoriteRepository.getFavoriteCntById(dto.getFno())
                .orElseThrow(()-> new IllegalArgumentException("요청하신 정보를 찾을 수 없습니다."));


        return FavoriteResponseDTO.builder()
                .favoriteDTOList(list.stream().map(f-> entityToDTO(f)).collect(Collectors.toList()))
                .favoriteCnt(favCnt).build();
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

    /**
     * 게시글 승인처리 기능
     * 승인 결과 리턴
     * @param fno
     * @return FundingDTO
     */
    @Override
    public FundingDTO updateAuthorized(Long fno) {

        Funding funding = fundingRepository.getFunding(fno).orElseThrow
                (()->new NullPointerException("요청하신 정보를 찾을 수 없습니다."));

            funding.changeAuthorized(true);
            Funding result = fundingRepository.save(funding);

        return entityToDTO(result);
    }

    /**
     * 승인되지 않은 게시글 리스트를 반환하는 기능
     * 관리자 페이지에서 사용
     * @return List<FundingDTO>
     */
    @Override
    public ListResponseDTO<FundingDTO> getNotAuthorizedFunding(ListRequestDTO dto) {

        Pageable pageable = dto.getPageable();

        Page<Funding> fundingList = fundingRepository.findAllByAuthorizedFalseAndRemovedFalse(pageable);

        List<FundingDTO> dtoList = fundingList.getContent().stream().map(list -> entityToDTO(list)).collect(Collectors.toList());

        PageMaker pageMaker = new PageMaker(dto.getPage(), dto.getSize(), (int) fundingList.getTotalElements());

        return ListResponseDTO.<FundingDTO>builder().listRequestDTO(dto)
                .dtoList(dtoList).pageMaker(pageMaker).build();
    }


}


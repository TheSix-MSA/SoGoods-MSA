package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.funding.common.dto.PageMaker;
import org.thesix.funding.dto.funding.FundingDTO;
import org.thesix.funding.dto.order.*;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Order;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;
import org.thesix.funding.repository.FundingRepository;
import org.thesix.funding.repository.OrderDetailsRepository;
import org.thesix.funding.repository.OrderRepository;
import org.thesix.funding.repository.ProductRepository;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final FundingService fundingService;
    private final ProductRepository productRepository;

    /***
     * 주문 insert 함수.
     * OrderRequestDTO에서 Order와 OrderDetails를 만든 후 저장한다.
     * 1+(상품종류) 만큼의 insert 발생
     * @param dto
     * @return
     */
    @Override
    public OrderResponseDTO save(OrderSaveRequestDTO dto) {
        if(dto.getReceiverRequest() == null || dto.getReceiverRequest().trim().equals("")){
            dto.setReceiverRequest("없음");
        }
        Order order = dtoToOrder(dto);
        order = orderRepository.save(order);

        if(dto.getProducts().size() == 0){
            throw new IllegalArgumentException("상품을 주문하지 않았습니다.");
            /***
             * 해당 익셉션을 BAD Request로 보내고 있다.. 설명이 필요한 부분.
             */
        }

        Product prod = null;
        OrderDetails details = null;
        for(Long i: dto.getProducts().keySet()){
            try {
                prod = Product.builder().pno(i).build();
                details = OrderDetails.builder().order(order)
                        .product(prod)
                        .numProds(dto.getProducts().get(i)).build();
                orderDetailsRepository.save(details);
            } catch (Exception e){
                throw new RuntimeException("존재하지 않는 상품입니다.");
            }
        }
        return entityToDto(order,dto.getProducts());
    }

    @Override
    public OrderPagingResponseDTO getUsersOrders(OrderPageRequestDTO dto) {
        if(dto.getPage() < 1) dto.setPage(1);

        Pageable pageable = PageRequest.of((dto.getPage()-1),10);
        Page<Object[]> res = orderRepository.getSearchedOrder(dto.getEmail(),dto.getSortCondition(),pageable);

        res.getContent().forEach(objects -> log.info(Arrays.toString(objects)));

        List<OrderBriefResponseDTO> dtoList = new ArrayList<>();

        PageMaker pageMaker = new PageMaker(dto.getPage(),5, (int)res.getTotalElements());

        res.getContent();
        if(res.getContent().size() == 0){
            return OrderPagingResponseDTO.builder().resDto(dtoList).pageMaker(pageMaker).build();
        }

        Order dtoRes = (Order)res.getContent().get(0)[0];
        int totalPrice = 0;
        String prodName = "";

        for(Object[] objArr: res.getContent()){
            if(!dtoRes.equals((Order)objArr[0])){
                dtoList.add(OrderBriefResponseDTO.builder()
                        .dto(entityToDto(dtoRes)).totalPrices(totalPrice).prodNames(prodName.substring(0,prodName.length()-2)).build());
                dtoRes = (Order)objArr[0];
                totalPrice = 0;
                prodName = "";
            }
            totalPrice += ((OrderDetails)objArr[1]).getNumProds()*((Product)objArr[2]).getPrice();
            prodName += ((Product)objArr[2]).getName()+", ";
        }

        dtoList.add(OrderBriefResponseDTO.builder()
                .dto(entityToDto(dtoRes)).totalPrices(totalPrice).prodNames(prodName.substring(0,prodName.length()-2)).build());

        return OrderPagingResponseDTO.builder().resDto(dtoList).pageMaker(pageMaker).build();
    }

    /***
     * 주문의 상세보기를 만드는 함수
     * 주문id(ono)를 받아 주문이 있나 확인 후 주문한 상품(들)의 정보와 함깨 리턴
     * @param ono
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponseDTO getOrder(Long ono) {
        List<Object[]> res = orderRepository.getDetailedOrder(ono);

        if(res.size()==0){
            throw new IllegalArgumentException("해당 결제 내역이 존재하지 않습니다.");
        }

        Order order = (Order)res.get(0)[0];
        Long totalPrice = 0L;
        Set<ProductInOrderDTO> prods = new HashSet<>();
        Funding fundInfo = (Funding)res.get(0)[3];

        for(Object[] obj: res){
            totalPrice += ((Product)obj[2]).getPrice();
            prods.add(produdctEntityToInOrderDTO((Product)obj[2], ((OrderDetails)obj[1]).getNumProds()));
        }

        return orderEntityToDetailedResponseDTO(order, prods, fundingService.entityToDTO(fundInfo));
    }

    /***
     * 주문 취소 함수
     * Order 엔티티의 canceledDate 에 현재 시각 추가
     * @param ono
     * @return
     */
    @Override
    @Transactional
    public OrderDetailResponseDTO cancelOrder(Long ono) {
        Order order = orderRepository.findById(ono).orElseThrow(() ->
                new NullPointerException("결제 내역이 존재하지 않습니다"));
        order.cancelOrder();

        return getOrder(ono);
    }

    /***
     * 주문이 배송이 되었을때 배송 날짜를 추가하는 함수
     * @param ono
     * @return
     */
    @Override
    public OrderDetailResponseDTO shipOrder(Long ono) {
        Order order = orderRepository.findById(ono).orElseThrow(() ->
                new NullPointerException("결제 내역이 존재하지 않습니다"));
        order.shipOrder();
        return getOrder(ono);
    }
}

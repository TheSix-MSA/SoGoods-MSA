package org.thesix.funding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thesix.funding.common.dto.ListResponseDTO;
import org.thesix.funding.dto.order.OrderPageRequestDTO;
import org.thesix.funding.dto.order.OrderSaveRequestDTO;
import org.thesix.funding.dto.order.OrderResponseDTO;
import org.thesix.funding.entity.Order;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;
import org.thesix.funding.repository.OrderDetailsRepository;
import org.thesix.funding.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    /***
     * 주문 insert 함수.
     * OrderRequestDTO에서 Order와 OrderDetails를 만든 후 저장한다.
     * 1+(상품종류) 만큼의 insert 발생
     * @param dto
     * @return
     */
    @Override
    public OrderResponseDTO save(OrderSaveRequestDTO dto) {
        if(dto.getReceiverRequest() == null || dto.getReceiverRequest().equals("")){
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

        for(Long i: dto.getProducts().keySet()){
            OrderDetails details = mapToOrderDetails(i, dto.getProducts(), order);
            orderDetailsRepository.save(details);
        }
        return entityToDto(order,dto.getProducts());
    }

    @Override
    public ListResponseDTO<OrderResponseDTO> getUsersOrders(OrderPageRequestDTO dto) {
        if(dto.getPage() < 1) dto.setPage(1);

        Pageable pageable = PageRequest.of((dto.getPage()-1),10);
        Page<Object[]> res = orderRepository.getSearchedOrder(dto.getEmail(),dto.getSortCondition(),pageable);

        List<OrderResponseDTO> dtoList = new ArrayList<>();
        List<Integer> totalPrices = new ArrayList<>();
        List<String> prodNames = new ArrayList<>();

        OrderResponseDTO dtoRes = entityToDto((Order)res.getContent().get(0)[0]);
        int totalPrice = 0;
        String prodName = "";

        for(Object[] objArr: res.getContent()){
            if(!dtoRes.equals(objArr[0])){
                dtoList.add(dtoRes);
                totalPrices.add(totalPrice);
                prodNames.add(prodName);
                dtoRes = entityToDto((Order)objArr[0]);
                totalPrice = 0;
                prodName = "";
            }
            totalPrice += ((OrderDetails)objArr[1]).getNumProds()*((Product)objArr[2]).getPrice();
            prodName += ((Product)objArr[2]).getName()+", ";
        }

        return null;
    }
}

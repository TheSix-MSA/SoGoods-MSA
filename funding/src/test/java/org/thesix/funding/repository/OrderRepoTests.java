package org.thesix.funding.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thesix.funding.dto.funding.FundingDTO;
import org.thesix.funding.dto.order.ProductInOrderDTO;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.Order;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;
import org.thesix.funding.service.OrderService;

import java.util.*;

@SpringBootTest
public class OrderRepoTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private FundingRepository fundingRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void insertTest() {
        Order order = Order.builder().buyer("판매자 놈").tid("결제수단")
                .receiverAddress("주소").receiverDetailedAddress("상세주소")
                .receiverName("받는 놈").receiverPhone("010-1234-5678")
                .receiverRequest("받는 놈 요청").build();

        orderRepository.save(order);
        Map<Long, Long> prods = new HashMap<>();
        prods.put(49L,2L);
        prods.put(169L,3L);

        for(Long i: prods.keySet()){
            OrderDetails details = OrderDetails.builder().order(order)
                    .product(Product.builder().pno(i).build())
                    .numProds(prods.get(i)).build();
            orderDetailsRepository.save(details);
        }
    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Object[]> res = orderRepository.getSearchedOrder("buyer@hogang.mem","",pageable);

        res.getContent().forEach(obj -> System.out.println(Arrays.toString(obj)));
    }

    @Test
    public void testReadOne(){
        List<Object[]> res = orderRepository.getDetailedOrder(1L);

        if(res.size()==0){
            throw new IllegalArgumentException("해당 결제 내역이 존재하지 않습니다.");
        }

        Order order = (Order)res.get(0)[0];
        Long totalPrice = 0L;
        Set<ProductInOrderDTO> prods = new HashSet<>();
        Funding fundInfo = (Funding)res.get(0)[3];

//                fundingRepository.getFundingById(((Product)res.get(0)[2])
//                .getFunding().getFno()).orElseThrow(() -> new NullPointerException("존재하지 않는 펀딩입니다"));

        System.out.println(fundInfo);

        for(Object[] obj: res){
            totalPrice += ((Product)obj[2]).getPrice();
            prods.add(orderService.produdctEntityToInOrderDTO((Product)obj[2], ((OrderDetails)obj[1]).getNumProds()));
        }
    }

    @Test
    public void orderDetailTest(){
        Product product = Product.builder().pno(83L).build();
//        List<OrderDetails> res = orderDetailsRepository.findByProduct(product);
        List<Object[]> res = orderRepository.getOrderInfoFromProduct(product);
        res.stream().forEach(r-> System.out.println(Arrays.toString(r)));
    }
}

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
        Order order = Order.builder().buyer("판매자 놈1").tid("T2922968078503176719")
                .receiverAddress("주소").receiverDetailedAddress("상세주소")
                .receiverName("받는 놈").receiverPhone("010-1234-5678")
                .receiverRequest("받는 놈 요청").kakaoPayOrderId("카카오").build();

        orderRepository.save(order);
        Map<Long, Long> prods = new HashMap<>();
        prods.put(601L,2L);
        prods.put(602L,2L);
        prods.put(603L,2L);

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


    @Test
    public void orderCancelTest() {
        Funding funding = Funding.builder()
                .title("삭제 테스트")
                .writer("작성자")
                .email("user@aaa.com")
                .content("내용....")
                .dueDate("2022-07-20 20:22:32")
                .success(false)
                .removed(false)
                .build();

        fundingRepository.save(funding);

        Product product = Product.builder()
                .name("삭제 테스트 상품 1")
                .des("1 번 굿즈입니다.")
                .price(1000)
                .funding(funding).build();

        productRepository.save(product);

        Product product1 = Product.builder()
                .name("삭제 테스트 상품 2")
                .des("2 번 굿즈입니다.")
                .price(1000)
                .funding(funding).build();

        productRepository.save(product1);

        Product product2 = Product.builder()
                .name("삭제 테스트 상품 3")
                .des("3 번 굿즈입니다.")
                .price(1000)
                .funding(funding).build();

        productRepository.save(product2);
    }
}

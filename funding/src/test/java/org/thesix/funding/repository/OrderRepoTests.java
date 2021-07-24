package org.thesix.funding.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thesix.funding.entity.Order;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class OrderRepoTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

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
}

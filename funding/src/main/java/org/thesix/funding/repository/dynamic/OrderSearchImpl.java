package org.thesix.funding.repository.dynamic;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.funding.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl(){super(Order.class);}

    @Override
    public Page<Object[]> getSearchedOrder(String email, String searchCond, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderDetails orderDetails = QOrderDetails.orderDetails;
        QProduct product = QProduct.product;

        JPQLQuery<Order> query = from(order);
        query.leftJoin(orderDetails).on(orderDetails.order.eq(order));
        query.leftJoin(product).on(orderDetails.product.eq(product));

        JPQLQuery<Tuple> tuple = query.select(order, orderDetails, product);

        if(searchCond!=null && !searchCond.equals("")){
            /***
             * 결제 조건 추가하는 부분. 일단 페이징부터 하고 해보자
             */
            System.out.println("Im here!");
        }

        tuple.where(order.buyer.eq(email));
        tuple.orderBy(order.modDate.desc());
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> list = tuple.fetch();

        List<Object[]> arrList = list.stream().map(tuple1-> tuple1.toArray()).collect(Collectors.toList());

        long totalCount = tuple.fetchCount();

        return new PageImpl<>(arrList, pageable, totalCount);
    }
}

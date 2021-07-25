package org.thesix.funding.repository.dynamic;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQuery;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.funding.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderSearchImpl extends QuerydslRepositorySupport implements OrderSearch {

    public OrderSearchImpl(){super(Order.class);}

    @Override
    public Page<Object[]> getSearchedOrder(String email, String sortCond, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderDetails orderDetails = QOrderDetails.orderDetails;
        QProduct product = QProduct.product;

        JPQLQuery<Order> query = from(order);
        query.leftJoin(orderDetails).on(orderDetails.order.eq(order));
        query.leftJoin(product).on(orderDetails.product.eq(product));

        JPQLQuery<Tuple> tuple = query.select(order, orderDetails, product);

        tuple.where(order.ono.gt(0));
        tuple.where(order.buyer.eq(email));
        tuple.orderBy(order.modDate.desc());
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> list = tuple.fetch();

        List<Object[]> arrList = list.stream().map(tuple1-> tuple1.toArray()).collect(Collectors.toList());

        long totalCount = tuple.fetchCount();

        return new PageImpl<>(arrList, pageable, totalCount);
    }

    @Override
    public List<Object[]> getDetailedOrder(Long ono) {
        QOrder order = QOrder.order;
        QOrderDetails orderDetails = QOrderDetails.orderDetails;
        QProduct product = QProduct.product;
        QFunding funding = QFunding.funding;

        JPQLQuery<Order> query = from(order);
        query.leftJoin(orderDetails).on(orderDetails.order.eq(order));
        query.leftJoin(product).on(orderDetails.product.eq(product));
        query.leftJoin(funding).on(product.funding.eq(funding));

        JPQLQuery<Tuple> tuple = query.select(order, orderDetails, product, funding);

        tuple.where(order.ono.eq(ono));

        List<Tuple> list = tuple.fetch();

        List<Object[]> arrList = list.stream().map(tuple1-> tuple1.toArray()).collect(Collectors.toList());

        return arrList;
    }

    @Override
    public List<Object[]> getOrderInfoFromProduct(Product prod) {
        QOrder order = QOrder.order;
        QOrderDetails orderDetails = QOrderDetails.orderDetails;
        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(orderDetails).on(orderDetails.product.eq(prod));
        query.leftJoin(order).on(orderDetails.order.eq(order), order.cancelledDate.isNull());

        JPQLQuery<Tuple> tuple = query.select(order, orderDetails.numProds);

        tuple.where(product.eq(prod));

        List<Tuple> list = tuple.fetch();

        List<Object[]> arrList = list.stream().map(tuple1-> tuple1.toArray()).collect(Collectors.toList());

        return arrList;
    }
}

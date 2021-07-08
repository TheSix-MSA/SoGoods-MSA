package org.thesix.funding.repository.dynamic;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.entity.QFavorite;
import org.thesix.funding.entity.QFunding;
import org.thesix.funding.entity.QProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SelectFundingImpl extends QuerydslRepositorySupport implements SelectFunding{


    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     */
    public SelectFundingImpl() {
        super(Funding.class);
    }

    /**
     * 아이디 값을 받아 글정보 + 제품정보를 반환하는 메서드
     * @param fno
     * @return List<Object[]>
     */
    public List<Object[]> getFundingById(Long fno){

        // querydsl을 이용해 쿼리 처리
        QFunding funding = QFunding.funding;
        QProduct product = QProduct.product;
        QFavorite favorite = QFavorite.favorite;

        JPQLQuery<Funding> query = from(funding);

        query.innerJoin(product).on(product.funding.eq(funding));
        JPQLQuery<Tuple> tuple = query.select(funding, product);
        tuple.where(funding.fno.eq(fno));
        tuple.orderBy(funding.fno.desc());

        List<Tuple> result = tuple.fetch();

        List<Object[]> list = result.stream().map(tuple1-> tuple1.toArray()).collect(Collectors.toList());

        System.out.println(result);

         return list;
    }
}

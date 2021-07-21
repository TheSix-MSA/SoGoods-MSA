package org.thesix.funding.repository.dynamic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.funding.entity.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.min;

public class FundingSearchImpl extends QuerydslRepositorySupport implements FundingSearch{

    /**
     * 상위클래스 생성자 생성
     */
    public FundingSearchImpl() {
        super(Funding.class);
    }

    /**
     * 검색 + 페이징 기능을 구현한 메서드
     * @param keyword
     * @param type
     * @param pageable
     * @return
     */
    @Override
    public Page<Object[]> getListSearch(String keyword, String type, Pageable pageable) {

        // querydsl을 이용해 쿼리 처리
        QFunding funding = QFunding.funding;
        QProduct product = QProduct.product;
        QFavorite favorite = QFavorite.favorite;

        JPQLQuery<Funding> query = from(funding);

        query.leftJoin(product).on(product.funding.eq(funding), product.removed.eq(false));
        query.leftJoin(favorite).on(favorite.funding.eq(funding));

        // 원하는 값만 select
        JPQLQuery<Tuple> tuple = query.select(funding, min(product.pno), favorite.countDistinct());

        // 동적 쿼리
        if(keyword != null && type != null) {

            BooleanBuilder condition = new BooleanBuilder();

            String typeArr[] = type.split("");

            for (String i : typeArr) {
                if (type.equals("t")) {
                    // type = title
                    condition.or(funding.title.contains(keyword));
                } else if (type.equals("w")) {
                    // type = writer
                    condition.or(funding.writer.contains(keyword));
                } else if (type.equals("c")) {
                    // type = content
                    condition.or(funding.content.contains(keyword));
                }
            }//end for

            tuple.where(condition);
        }

        tuple.where(funding.fno.gt(0L));
        tuple.where(funding.removed.eq(false));
        tuple.where(funding.authorized.eq(true));
        tuple.groupBy(funding);
        tuple.orderBy(funding.fno.desc());

        // 페이징 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> list = tuple.fetch();

        List<Object[]> arrList = list.stream().map(tuple1-> tuple1.toArray()).collect(Collectors.toList());

        long totalCount = tuple.fetchCount();

        return new PageImpl<>(arrList, pageable, totalCount);
    }



}

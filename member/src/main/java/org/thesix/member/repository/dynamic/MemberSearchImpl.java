package org.thesix.member.repository.dynamic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.member.entity.Member;
import org.thesix.member.entity.MemberRole;
import org.thesix.member.entity.QMember;
import org.thesix.member.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch {

    public MemberSearchImpl() {
        super(Member.class);
    }

    @Override
    public Page<Object[]> getMemberList(String type, String keyword, Pageable pageable) {

        QMember member = QMember.member;

        JPQLQuery<Member> query = from(member);

        JPQLQuery<Tuple>  tuple = query.select(member, member.roleSet.size());

        if(keyword != null && type != null){
            BooleanBuilder condition = new BooleanBuilder();

            String[] typeArr = type.split("");

            for (String searchType : typeArr) {
                if (searchType.equals("name")) {
                    condition.or(member.name.contains(keyword));
                } else if (searchType.equals("email")) {
                    condition.or(member.email.contains(keyword));
                } else if (searchType.equals("address")) {
                    condition.or(member.address.contains(keyword));
                }
            }

            tuple.where(condition);
        }


        tuple.limit(pageable.getPageSize());
        tuple.offset(pageable.getOffset());

        List<Object[]> result = tuple.fetch().stream().map(t -> t.toArray()).collect(Collectors.toList());

        return new PageImpl<>(result,pageable,query.fetchCount());
    }
}

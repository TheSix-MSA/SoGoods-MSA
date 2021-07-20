package org.thesix.member.repository.dynamic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.member.dto.MemberDTO;
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

    /**
     * 동적쿼리 적용 paging 회원목록리스트 반환함.
     *
     * @param type
     * @param keyword
     * @param pageable
     * @return
     */
    @Override
    public Page<Object> getMemberList(String type, String keyword, Pageable pageable) {

        QMember member = QMember.member;

        JPQLQuery<Member> query = from(member);

        if(keyword != null && type != null){
            BooleanBuilder condition = new BooleanBuilder();

            String[] typeArr = type.split("");

            for (String searchType : typeArr) {
                if (searchType.equals("n")) {
                    condition.or(member.name.contains(keyword));
                } else if (searchType.equals("e")) {
                    condition.or(member.email.contains(keyword));
                } else if (searchType.equals("a")) {
                    condition.or(member.address.contains(keyword));
                }
            }

            query.where(condition);
        }


        query.orderBy(member.email.desc());

        query.limit(pageable.getPageSize());
        query.offset(pageable.getOffset());

        List<MemberDTO> result = query.fetch().stream().map(memberObj ->
                MemberDTO.builder()
                        .email(memberObj.getEmail())
                        .name(memberObj.getName())
                        .gender(memberObj.getGender())
                        .birth(memberObj.getBirth())
                        .phone(memberObj.getPhone())
                        .address(memberObj.getAddress())
                        .detailAddress(memberObj.getDetailAddress())
                        .removed(memberObj.isRemoved())
                        .banned(memberObj.isBanned())
                        .provider(memberObj.getProvider())
                        .social(memberObj.isSocial())
                        .regDate(memberObj.getRegDate())
                        .roleSet(memberObj.getRoleSet())
                        .build()
        ).collect(Collectors.toList());


        return new PageImpl(result, pageable, query.fetchCount());
    }
}

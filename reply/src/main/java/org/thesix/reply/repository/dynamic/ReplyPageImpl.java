package org.thesix.reply.repository.dynamic;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.reply.entity.QReplies;
import org.thesix.reply.entity.Replies;

import java.util.List;
import java.util.stream.Collectors;

public class ReplyPageImpl extends QuerydslRepositorySupport implements ReplyPage{

    public ReplyPageImpl() {
        super(Replies.class);
    }

    @Override
    public Page<Replies> getList(Long bno, Pageable pageable) {
        QReplies replies = QReplies.replies;
        JPQLQuery<Replies> query = from(replies);

        query.where(replies.rno.gt(0));
        query.where(replies.keyValue.eq(bno));
        query.orderBy(replies.groupId.asc(), replies.parentId.asc());
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());
        List<Replies> tupList = query.fetch();

        long totalCnt = query.fetchCount();

        return new PageImpl<>(tupList, pageable, totalCnt);
    }
}

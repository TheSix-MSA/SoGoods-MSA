package org.thesix.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.board.entity.Board;
import org.thesix.board.entity.QBoard;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> getBoardList(String type, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = board.bno.gt(0L);
        booleanBuilder.and(booleanExpression);

        if(type != null && keyword != null && keyword.trim().length()>0) {
            String[] arrStr = type.split("");
            BooleanBuilder condition = new BooleanBuilder();
            for(String t: arrStr) {
                if(t.equals("t")) {
                    condition.or(board.title.contains(keyword));
                } else if (t.equals("w")) {
                    condition.or(board.writer.contains(keyword));
                } else if (t.equals("c")) {
                    condition.or(board.content.contains(keyword));
                } else if (t.equals("tc")) {
                    condition.or(board.title.contains(keyword).or(board.content.contains(keyword)));
                }
            }
            query.where(condition);
        }

        query.where(board.bno.gt(0L));
        query.groupBy(board);
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        List<Board> listResult = query.fetch();
        log.info(listResult);
        long totalCount = query.fetchCount();

        return new PageImpl<>(listResult,pageable,totalCount);
    }
}

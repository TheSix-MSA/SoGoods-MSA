package org.thesix.board.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.board.entity.Board;
import org.thesix.board.entity.QBoard;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Object[]> searchPageWithList(String type, String keyword, Pageable pageable) {
        log.info("searchPageWithList----------------------");

        QBoard board = QBoard.board;

        JPQLQuery<Board> query = from(board);

        return null;
    }
}

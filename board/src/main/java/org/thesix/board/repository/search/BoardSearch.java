package org.thesix.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.board.entity.Board;

public interface BoardSearch {

    /*
        게시글의 종류에 따라 목록을 가져옴
     */
    Page<Board> getBoardList(String boardType, String type, String keyword, Pageable pageable);

    /*
        자신이 작성한 게시글의 목록을 가져옴
     */
    Page<Board> getWriterBoardList(String writer, String type, String keyword, Pageable pageable);
}

package org.thesix.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thesix.board.entity.Board;
import org.thesix.board.entity.BoardType;
import org.thesix.board.repository.search.BoardSearch;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    /*
        게시판 종류별 목록 가져오기
     */
    @EntityGraph(attributePaths = "boardType", type= EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Board b where b.boardType = :board_type")
    Optional<Board> findByBoard(String board_type);
    
    /*
        자신이 작성한 글 목록 가져오기
     */
    @EntityGraph(attributePaths = "writer", type= EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Board b where b.writer = :writer")
    Optional<Board> findByWriterBoard(String writer);

    @EntityGraph(attributePaths = "boardType", type= EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Board b where b.email = :email and b.boardType = :boardType and b.removed = false")
    Page<Board> findByBoardWith(String email, BoardType boardType, Pageable pageable);

    @Query("SELECT COUNT(b.bno) FROM Board b GROUP BY b.boardType")
    Long[] countTotalBoard();

}
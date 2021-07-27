package org.thesix.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thesix.board.entity.Board;
import org.thesix.board.repository.search.BoardSearch;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

//    @Query("select b from Board b where b.type = :type")
//    List<Object[]> getAllBoardList(@Param("type") String type);
//
//    @Query("select b from Board b where b.type = :type")
//    Page<Object[]> getBoardList(Pageable pageable);

    @EntityGraph(attributePaths = "boardType", type= EntityGraph.EntityGraphType.LOAD)
    @Query("select b from Board b where b.boardType = :board_type")
    Optional<Board> findByBoard(String board_type);

}
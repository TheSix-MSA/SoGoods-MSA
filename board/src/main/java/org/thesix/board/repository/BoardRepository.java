package org.thesix.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
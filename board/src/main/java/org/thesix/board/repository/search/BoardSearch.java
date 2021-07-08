package org.thesix.board.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.board.entity.Board;

public interface BoardSearch {

    Page<Object[]> searchPageWithList(String type, String keyword, Pageable pageable);
}

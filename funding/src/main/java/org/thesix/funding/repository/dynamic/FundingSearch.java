package org.thesix.funding.repository.dynamic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FundingSearch {

    /**
     * 검색 + 페이징 기능을 수행하는 추상메서드
     * @param keyword
     * @param type
     * @param pageable
     * @return
     */
    Page<Object[]> getListSearch(String keyword, String type, Pageable pageable);
}

package org.thesix.funding.repository.dynamic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderSearch {
    Page<Object[]> getSearchedOrder(String email,  String searchCond, Pageable pageable);
}

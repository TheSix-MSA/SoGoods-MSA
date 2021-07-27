package org.thesix.funding.repository.dynamic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.funding.entity.Product;

import java.util.List;

public interface OrderSearch {
    Page<Object[]> getSearchedOrder(String email,  String searchCond, Pageable pageable);
    List<Object[]> getDetailedOrder(Long ono);
    List<Object[]> getOrderInfoFromProduct(Product prod);
}

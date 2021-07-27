package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.funding.entity.Order;
import org.thesix.funding.repository.dynamic.OrderSearch;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderSearch {
}

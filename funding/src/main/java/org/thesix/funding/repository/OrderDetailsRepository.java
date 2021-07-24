package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.funding.entity.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
}
package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
}

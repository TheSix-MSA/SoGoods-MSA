package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thesix.funding.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository< Product,Long> {

    @Query("select p from Product p where p.funding.fno = :fno")
    Optional<Product[]> findByFundingId(Long fno);

}

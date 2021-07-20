package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.thesix.funding.entity.Product;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.funding.fno = :fno and p.removed=false")
    Optional<List<Product>> getProductById(Long fno);

    @Modifying
    @Transactional
    @Query("delete from Product p where p.funding.fno = :fno and p.removed=false")
    Optional<Integer> deleteAllByFundingId(Long fno);


}

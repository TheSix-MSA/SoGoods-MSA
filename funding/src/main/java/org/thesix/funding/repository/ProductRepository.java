package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thesix.funding.entity.OrderDetails;
import org.thesix.funding.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.funding.fno = :fno and p.removed=false")
    Optional<List<Product>> getProductById(Long fno);

    @Query("select p from Product p where p.funding.fno = :fno")
    Optional<List<Product>> getALLProductById(Long fno);

    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> getOneProduct(Long pno);

    @Query("select p from Product p where p.funding.fno = :fno")
    Optional<List<Product>> getPnoList(Long fno);

}

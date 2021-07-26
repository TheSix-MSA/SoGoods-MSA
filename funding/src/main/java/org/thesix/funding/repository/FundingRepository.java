package org.thesix.funding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.repository.dynamic.FundingSearch;
import java.util.List;
import java.util.Optional;

public interface FundingRepository extends JpaRepository<Funding, Long>, FundingSearch {

    @Query("select f, count(distinct p), count(distinct fa) " +
            "from Funding f left join Product p on f.fno=p.funding.fno " +
            "left join Favorite fa on f.fno=fa.funding.fno " +
            "group by f order by f.fno desc ")
    Page<Object[]> getData(Pageable pageable);


    @Query("select f from Funding f where f.fno=:fno and f.removed=false and f.authorized=true")
    Optional<Funding> getFundingById(Long fno);

    @Query("select f from Funding f where f.fno=:fno and f.removed=false and f.success=false")
    Optional<Funding> getFunding(Long fno);

    @Query("select f from Funding f inner join Favorite fa on f.fno=fa.funding.fno where fa.actor=:email")
    Optional<List<Funding>> getFavoriteFundingByEmail(String email);


    @Query("select f from Funding f where f.email=:email and f.removed=false")
    Optional<List<Funding>> getFundingListByEmail(String email);

    @Query("select p,f " +
            "from Product p " +
            "left join Funding f on p.funding.fno =f.fno " +
            "where p.funding.fno = :fno and p.removed=false group by p")
    Optional<List<Object[]>> getFundingALLData(Long fno);

    Page<Funding> findAllByAuthorizedFalseAndRemovedFalseAndRequestApprovalTrue(Pageable pageable);
}

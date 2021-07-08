package org.thesix.funding.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.entity.Funding;
import org.thesix.funding.repository.dynamic.FundingSearch;
import org.thesix.funding.repository.dynamic.SelectFunding;

import java.util.List;
import java.util.Optional;

public interface FundingRepository extends JpaRepository<Funding, Long>, FundingSearch, SelectFunding {

    @Query("select f, count(distinct p), count(distinct fa) " +
            "from Funding f left join Product p on f.fno=p.funding.fno " +
            "left join Favorite fa on f.fno=fa.funding.fno " +
            "group by f order by f.fno desc ")
    Page<Object[]> getData(Pageable pageable);





}

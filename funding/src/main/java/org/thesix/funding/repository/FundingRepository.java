package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.funding.entity.Funding;

public interface FundingRepository extends JpaRepository<Funding, Long> {


}

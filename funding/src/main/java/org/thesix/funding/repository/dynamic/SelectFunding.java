package org.thesix.funding.repository.dynamic;

import org.thesix.funding.dto.ListFundingDTO;

import java.util.List;

public interface SelectFunding {

   public List<Object[]> getFundingById(Long fno);
}

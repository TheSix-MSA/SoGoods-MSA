package org.thesix.funding.repository.dynamic;

import org.thesix.funding.dto.ListFundingDTO;
import org.thesix.funding.entity.Funding;

import java.util.List;
import java.util.Optional;

public interface SelectFunding {

   public Object getProductById(Long fno);
}

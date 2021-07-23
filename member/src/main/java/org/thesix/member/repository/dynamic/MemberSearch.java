package org.thesix.member.repository.dynamic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberSearch{
    Page<Object> getMemberList(String type, String keyword, Pageable pageable, boolean approval);

}

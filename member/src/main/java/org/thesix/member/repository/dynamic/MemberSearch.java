package org.thesix.member.repository.dynamic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;
import org.thesix.member.entity.Member;

public interface MemberSearch{
    Page<Object[]> getMemberList(String type, String keyword, Pageable pageable);

}

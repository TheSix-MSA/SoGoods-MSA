package org.thesix.reply.repository.dynamic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thesix.reply.entity.Replies;

public interface ReplyPage {
    Page<Replies> getList(Long bno, Pageable pageable);
}

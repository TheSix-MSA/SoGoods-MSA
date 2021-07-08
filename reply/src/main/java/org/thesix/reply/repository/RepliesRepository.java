package org.thesix.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.reply.entity.Replies;
import org.thesix.reply.repository.dynamic.ReplyPage;

public interface RepliesRepository extends JpaRepository<Replies, Long>, ReplyPage {
}

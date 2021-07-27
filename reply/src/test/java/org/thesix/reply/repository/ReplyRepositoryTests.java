package org.thesix.reply.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thesix.reply.entity.Replies;

import java.util.*;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {
    @Autowired
    private RepliesRepository repliesRepository;

    @Test
    public void testsInsert(){
        IntStream.rangeClosed(1, 2).forEach(i -> {
            Replies reply = Replies.builder()
                    .keyValue(1L)
                    .content("'대' 댓글 "+i)
                    .email("writerR"+i+"@email.com")
                    .writer("'대' 댓글 작성자"+i)
                    .groupId(1L)
                    .level(2L)
                    .parentId(31L)
                    .build();

            repliesRepository.save(reply);
        });
    }

    @Test
    public void testPagingReplies(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("groupId"));

        Page<Replies> res = repliesRepository.getList(1L ,pageable);

        res.getContent().forEach(replies -> {
            log.info(replies);
        });
    }
}

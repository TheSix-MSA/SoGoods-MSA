package org.thesix.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.entity.Board;
import org.thesix.board.service.BoardService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepoTests {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void connectTest() {
        log.info(boardRepository.getClass().getName());
    }

    @Test
    public void testSelect() {
        List<Board> boards = boardRepository.findAll();
        boards.forEach(a -> log.info(a));
    }

    @Test
    public void registerTest() {
        IntStream.rangeClosed(1, 50).forEach(i-> {
            BoardDTO dto = BoardDTO.builder()
                    .title("테스트 제목 " + i)
                    .writer("테스트 작성자 " + i)
                    .email("테스트 이메일 " + i)
                    .content("테스트 내용 " + i)
                    .type("테스트 타입 " + i)
                    .build();
            Long bno = boardService.register(dto);
        });

    }
}

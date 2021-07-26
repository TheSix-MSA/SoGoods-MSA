package org.thesix.board.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.entity.Board;
import org.thesix.board.entity.BoardType;
import org.thesix.board.service.BoardService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        IntStream.rangeClosed(1, 30).forEach(i-> {
            BoardDTO dto = BoardDTO.builder()
                    .title("테스트 제목 " + i)
                    .writer("테스트 작성자 " + i)
                    .email("테스트 이메일 " + i)
                    .content("테스트 내용 " + i)
                    .replyCnt(3L)
                    .build();
            BoardDTO registerDTO = boardService.register(dto, "NOVELIST");
        });
    }

    @Test
    public void testGetBoardList() {
        String boardType = "NOVELIST";
        Pageable pageable = PageRequest.of(0,10);
        String keyword = "10";
        String type = "t";

        Page<Board> list = boardRepository.getBoardList(boardType, type, keyword, pageable);
        list.getContent().forEach(i -> System.out.println(i));
    }

    @Test
    public void testWriter() {
        String boardType = "NOTICE";
        BoardType boardCate = BoardType.valueOf(boardType);
        String writer = "테스트 작성자 1";

        Page<Board> list = boardRepository.findByBoardWith(writer,boardCate,PageRequest.of(0, 10, Sort.by("bno").descending()));
        list.getContent().forEach(i -> System.out.println(i));
    }

    @Test
    public void countTest() {
        Long[] result = boardRepository.countTotalBoard();
        Map<String,Long> resultDto = new HashMap<>();
        resultDto.put("FREE",result[0]);
        resultDto.put("NOTICE",result[1]);
        resultDto.put("NOVELIST",result[2]);
        System.out.println(Arrays.toString(result));
    }
}

package org.thesix.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시판 글등록("/board/register")
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody BoardDTO dto) {
        Long bno = boardService.register(dto);
        log.info("Register BNO:" + bno);
        return ResponseEntity.ok().body(bno);
    }

    // 게시판 글수정("/board/modify/{bno}")
    @PutMapping("/modify/{bno}")
    public ResponseEntity<BoardDTO> modify(@PathVariable Long bno, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        log.info("Modify BNO: " + bno);
        BoardDTO modifyDTO = boardService.modify(dto);
        return ResponseEntity.ok(modifyDTO);
    }

    // 게시판 글삭제("/board/remove/{bno}")
    @DeleteMapping("/remove/{bno}")
    public ResponseEntity<BoardDTO> removed(@PathVariable Long bno, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        BoardDTO removedDTO = boardService.remove(dto);
        log.info("Remove BNO: " + bno);
        return ResponseEntity.ok().body(removedDTO);
    }

    // 게시판 특정 글조회("/board/read/{bno}")
    @GetMapping("/read/{bno}")
    public ResponseEntity<BoardDTO> read(@PathVariable Long bno) {
        BoardDTO readDTO  = boardService.read(bno);
        log.info("Read DTO: " + readDTO);
        return ResponseEntity.ok().body(readDTO);
    }

    // 게시판 글목록("/board/list")
    // @GetMapping("/list")

}

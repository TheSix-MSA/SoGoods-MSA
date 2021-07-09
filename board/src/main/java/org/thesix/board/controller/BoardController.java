package org.thesix.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListDTO;
import org.thesix.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시판 글등록("/board/register")
    @PostMapping("/{type}/register")
    public ResponseEntity<BoardDTO> register(@RequestBody BoardDTO dto, @PathVariable String type) {
        BoardDTO registerDTO = boardService.register(dto);
        log.info("Register BNO:" + registerDTO);
        return ResponseEntity.ok().body(registerDTO);
    }

    // 게시판 글수정("/board/modify/{bno}")
    @PutMapping("/{type}/modify/{bno}")
    public ResponseEntity<BoardDTO> modify(@PathVariable Long bno, @PathVariable String type, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        log.info("Modify BNO: " + bno);
        BoardDTO modifyDTO = boardService.modify(dto);
        return ResponseEntity.ok(modifyDTO);
    }

    // 게시판 글삭제("/board/remove/{bno}")
    @DeleteMapping("/{type}/remove/{bno}")
    public ResponseEntity<BoardDTO> removed(@PathVariable Long bno, @PathVariable String type, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        BoardDTO removedDTO = boardService.remove(dto);
        log.info("Remove BNO: " + bno);
        return ResponseEntity.ok().body(removedDTO);
    }

    // 게시판 특정 글조회("/board/read/{bno}")
    @GetMapping("/{type}/read/{bno}")
    public ResponseEntity<BoardDTO> read(@PathVariable Long bno, @PathVariable String type) {
        BoardDTO readDTO  = boardService.read(bno);
        log.info("Read DTO: " + readDTO);
        return ResponseEntity.ok().body(readDTO);
    }

    // 게시판 글목록("/board/list")
    @GetMapping("/{type}/list")
    public ResponseEntity<BoardDTO> getList(@PathVariable String type) {
        BoardDTO boardListDTO = boardService.boardList(type);
        log.info("BoardListDTO: " + boardListDTO);
        return ResponseEntity.ok().body(boardListDTO);
    }


}

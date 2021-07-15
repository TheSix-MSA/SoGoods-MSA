package org.thesix.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListRequestDTO;
import org.thesix.board.dto.BoardListResponseDTO;
import org.thesix.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시판 글등록("/board/register")
    @PostMapping("/{board_type}/register")
    public ResponseEntity<BoardDTO> register(@RequestBody BoardDTO dto, @PathVariable String board_type) {
        BoardDTO registerDTO = boardService.register(dto, board_type);
        log.info("Register BNO:" + registerDTO);
        return ResponseEntity.ok().body(registerDTO);
    }

    // 게시판 글수정("/board/modify/{bno}")
    @PutMapping("/{board_type}/modify/{bno}")
    public ResponseEntity<BoardDTO> modify(@PathVariable Long bno, @PathVariable String board_type, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        log.info("Modify BNO: " + bno);
        BoardDTO modifyDTO = boardService.modify(dto);
        return ResponseEntity.ok(modifyDTO);
    }

    // 게시판 글삭제("/board/remove/{bno}")
    @DeleteMapping("/{board_type}/remove/{bno}")
    public ResponseEntity<BoardDTO> removed(@PathVariable Long bno, @PathVariable String board_type, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        BoardDTO removedDTO = boardService.remove(dto);
        log.info("Remove BNO: " + bno);
        return ResponseEntity.ok().body(removedDTO);
    }

    // 게시판 특정 글조회("/board/board_type/read/{bno}")
    @GetMapping("/{board_type}/read/{bno}")
    public ResponseEntity<BoardDTO> read(@PathVariable Long bno, @PathVariable String board_type) {
        BoardDTO readDTO  = boardService.read(bno);
        log.info("Read DTO: " + readDTO);
        return ResponseEntity.ok().body(readDTO);
    }

    // 게시판 글목록("/board/board_type/list")
    @GetMapping("/{board_type}/list")
    public ResponseEntity<BoardListResponseDTO<BoardDTO>> getList(BoardListRequestDTO boardListRequestDTO,
                                                                  @PathVariable String board_type) {
        BoardListResponseDTO<BoardDTO> boardListDTO = boardService.getList(boardListRequestDTO, board_type);
        log.info("BoardListDTO: " + boardListDTO);
        return ResponseEntity.ok(boardService.getList(boardListRequestDTO, board_type));
    }


}

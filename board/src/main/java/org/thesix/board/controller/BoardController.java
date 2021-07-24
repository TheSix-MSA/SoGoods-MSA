package org.thesix.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListRequestDTO;
import org.thesix.board.dto.BoardListResponseDTO;
import org.thesix.board.service.BoardService;

import static org.thesix.board.util.ApiUtil.ApiResult;

import static org.thesix.board.util.ApiUtil.success;

@RestController
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /*
        게시판 글등록("/board/{boardType}")
     */
    @PostMapping("/{boardType}")
    public ApiResult<BoardDTO> register(@RequestBody BoardDTO dto, @PathVariable String boardType) {
        BoardDTO registerDTO = boardService.register(dto, boardType);
        log.info(boardType + "   00000000000");
        log.info("Register BNO:" + registerDTO);
        return success(registerDTO);
    }

    /*
        게시판 글수정("/board/{boardType}/{bno}")
     */
    @PutMapping("/{boardType}/{bno}")
    public ApiResult<BoardDTO> modify(@PathVariable Long bno, @PathVariable String boardType, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        log.info("Modify BNO: " + bno);
        BoardDTO modifyDTO = boardService.modify(dto);
        return success(modifyDTO);
    }

    /*
        게시판 글삭제("/board/{boardType}/{bno}")
     */
    @DeleteMapping("/{boardType}/{bno}")
    public ApiResult<BoardDTO> removed(@PathVariable Long bno, @PathVariable String boardType) {
        BoardDTO removedDTO = boardService.remove(bno, boardType);
        log.info("Remove BNO: " + bno);
        return success(removedDTO);
    }

    /*
        게시판 특정 글조회("/board/{boardType}/{bno}")
     */
    @GetMapping("/{boardType}/{bno}")
    public ApiResult<BoardDTO> read(@PathVariable Long bno, @PathVariable String boardType) {
        BoardDTO readDTO  = boardService.read(bno);
        log.info("Read DTO: " + readDTO);
        return success(readDTO);
    }

    /*
        공지사항 공개/비공개
     */
    @PutMapping("/{boardType}/isPrivate/{bno}")
    public ApiResult<BoardDTO> notice(@PathVariable String boardType, @PathVariable Long bno, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        BoardDTO noticeDTO = boardService.changeIsPrivate(dto, boardType);
        return success(noticeDTO);
    }

    /*
        게시판 글목록("/board/{boardType}/list")
     */
    @GetMapping("/{boardType}/list")
    public ApiResult<BoardListResponseDTO<BoardDTO>> getList(BoardListRequestDTO boardListRequestDTO,
                                                             @PathVariable String boardType) {
        BoardListResponseDTO<BoardDTO> boardListDTO = boardService.getList(boardListRequestDTO, boardType);
        log.info("BoardListDTO: " + boardListDTO);
        return success(boardService.getList(boardListRequestDTO, boardType));
    }


    /*
        자신이 작성한 게시글 목록("/board/{writer}")
     */
    @GetMapping("/{writer}")
    public ApiResult<BoardListResponseDTO<BoardDTO>> writerList(BoardListRequestDTO boardListRequestDTO, @PathVariable String writer) {
        BoardListResponseDTO<BoardDTO> writerListDTO = boardService.writerList(boardListRequestDTO, writer);
        log.info("WriterBoardDTO: " + writerListDTO);
        return success(writerListDTO);
    }

    /*
        댓글 증가("/board/countUp/{bno}")
     */
    @PutMapping("/countUp/{bno}")
    public ApiResult<BoardDTO> replyCountUp(@PathVariable Long bno, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        BoardDTO countUpDTO = boardService.replyCountUp(bno, dto);
        return success(countUpDTO);
    }

    /*
        댓글 감소("/board/countDown/{bno}")
     */
    @PutMapping("/countDown/{bno}")
    public ApiResult<BoardDTO> replyCountDown(@PathVariable Long bno, @RequestBody BoardDTO dto) {
        dto.setBno(bno);
        BoardDTO countDownDTO = boardService.replyCountDown(bno, dto);
        return success(countDownDTO);
    }
}

package org.thesix.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListRequestDTO;
import org.thesix.board.dto.BoardListResponseDTO;
import org.thesix.board.dto.PageMaker;
import org.thesix.board.entity.Board;
import org.thesix.board.entity.BoardType;
import org.thesix.board.repository.BoardRepository;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    /*
        게시판 글등록("/board/{boardType}")
     */
    @Override
    public BoardDTO register(BoardDTO dto, String boardType) {
        Board register = dtoToEntity(dto, boardType);
        Board registerResult = boardRepository.save(register);
        return entityToDTO(registerResult);
    }

    /*
        게시판 글수정("/board/{boardType}/{bno}")
     */
    @Override
    public BoardDTO modify(BoardDTO dto) {
        Board result = boardRepository.findById(dto.getBno()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if(dto.isRemoved() == false) {
            result.changeTitle(dto.getTitle());
            result.changeContent(dto.getContent());

            Board modifyResult = boardRepository.save(result);
            return entityToDTO(modifyResult);
        }
        throw new RuntimeException("이미 삭제된 게시글입니다.");
    }

    /*
        게시판 글삭제("/board/{bno}")
     */
    @Override
    @Transactional
    public BoardDTO remove(Long bno, String boardType) {
        Board result = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        result.changeRemoved(true);
        Board removeResult = boardRepository.save(result);
        return entityToDTO(removeResult);
    }
    /*
        공지사항 공개/비공개 처리
     */
    @Override
    public BoardDTO changeIsPrivate(BoardDTO dto, String boardType) {
        Board result = boardRepository.findById(dto.getBno()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
        if(boardType.equals("NOTICE")) {
            result.changeIsPrivate(!result.isPrivate());
            Board noticeResult = boardRepository.save(result);
            return entityToDTO(noticeResult);
        }
        throw new RuntimeException("공개되지 않는 게시글입니다.");
    }

    /*
        게시판 특정 글조회("/board/{board_type}/{bno}")
     */
    @Override
    public BoardDTO read(Long bno) {

        Board result = boardRepository.findById(bno).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return entityToDTO(result);

    }

    /*
        특정 게시판의 목록("/board/{board_type}/list")
     */
    @Override
    public BoardListResponseDTO<BoardDTO> getList(BoardListRequestDTO boardListRequestDTO, String boardType) {
        log.info(boardListRequestDTO + " : " + boardType);
        Page<Board> list = boardRepository.getBoardList(
                boardType,
                boardListRequestDTO.getType(),
                boardListRequestDTO.getKeyword(),
                boardListRequestDTO.getPageable()
        );
        BoardListResponseDTO boardListResponseDTO = BoardListResponseDTO.builder()
                .pageMaker(new PageMaker(boardListRequestDTO.getPageable(), (int) list.getTotalElements()))
                .boardDtoList(list.stream().map((board) -> entityToDTO(board)).collect(Collectors.toList()))
                .boardListRequestDTO(boardListRequestDTO)
                .build();
        return boardListResponseDTO;
    }

    /*
        자신이 작성한 게시글 목록("/board/{writer}")
     */
    @Override
    public BoardListResponseDTO<BoardDTO> writerList(BoardListRequestDTO boardListRequestDTO, String writer) {
        log.info(boardListRequestDTO + " : " + writer);
        Page<Board> list = boardRepository.getWriterBoardList(
                writer,
                boardListRequestDTO.getType(),
                boardListRequestDTO.getKeyword(),
                boardListRequestDTO.getPageable()
        );

        BoardListResponseDTO boardListResponseDTO = BoardListResponseDTO.builder()
                .pageMaker(new PageMaker(boardListRequestDTO.getPageable(), (int) list.getTotalElements()))
                .boardDtoList(list.stream().map((board)->entityToDTO(board)).collect(Collectors.toList()))
                .boardListRequestDTO(boardListRequestDTO)
                .build();

        return boardListResponseDTO;
    }

    /*
        댓글 증가
     */
    @Override
    public BoardDTO replyCountUp(Long bno, BoardDTO dto) {
        Board result = boardRepository.findById(dto.getBno()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
        result.replyCountUp(result.getReplyCnt());
        Board countResult = boardRepository.save(result);
        return entityToDTO(countResult);
    }

    @Override
    public BoardDTO replyCountDown(Long bno, BoardDTO dto) {
        Board result = boardRepository.findById(dto.getBno()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 게시글입니다."));
        result.replyCountDown(result.getReplyCnt());
        Board countResult = boardRepository.save(result);
        return entityToDTO(countResult);
    }

}

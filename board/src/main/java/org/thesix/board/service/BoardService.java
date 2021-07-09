package org.thesix.board.service;

import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListDTO;
import org.thesix.board.entity.Board;

import java.util.List;

public interface BoardService {

    // 게시판 글작성
    BoardDTO register(BoardDTO dto);

    // 게시판 글수정
    BoardDTO modify(BoardDTO dto);

    // 게시판 글삭제
    BoardDTO remove(BoardDTO dto);

    // 게시판 특정 글조회
    BoardDTO read(Long bno);

    // 특정 게시판의 목록 가져오기
    BoardDTO boardList(String type);

    // DTO 객체를 ENTITY로 변환
    default Board dtoToEntity(BoardDTO dto) {
        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .email(dto.getEmail())
                .content(dto.getContent())
                .type(dto.getType())
                .build();
        return board;
    }

    // ENTITY 객체를 DTO 객체로 변환
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .email(board.getEmail())
                .content(board.getContent())
                .removed(board.isRemoved())
                .type(board.getType())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
        return boardDTO;
    }
}

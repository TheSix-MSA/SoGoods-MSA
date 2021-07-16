package org.thesix.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListRequestDTO;
import org.thesix.board.dto.BoardListResponseDTO;
import org.thesix.board.dto.PageMaker;
import org.thesix.board.entity.Board;
import org.thesix.board.repository.BoardRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // 게시판 글등록("/board/{board_type}/register")
    @Override
    public BoardDTO register(BoardDTO dto, String board_type) {
        Board register = dtoToEntity(dto, board_type);
        Board registerResult = boardRepository.save(register);
        return entityToDTO(registerResult);
    }

    // 게시판 글수정("/board/modify/{bno}")
    @Override
    public BoardDTO modify(BoardDTO dto) {
        Optional<Board> result = boardRepository.findById(dto.getBno());
        if(result.isPresent() && dto.isRemoved() == false) {
            Board entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            Board modifyResult = boardRepository.save(entity);
            return entityToDTO(modifyResult);
        }
        return null;
    }

    // 게시판 글삭제("/board/remove/{bno}")
    @Override
    public BoardDTO remove(BoardDTO dto) {
        Optional<Board> result = boardRepository.findById(dto.getBno());
        if(result.isPresent() && dto.isRemoved() == false) {
            Board entity = result.get();
            entity.changeRemoved(true);
            Board removeResult = boardRepository.save(entity);
            return entityToDTO(removeResult);
        }
        return null;
    }

    // 게시판 특정 글조회("/board/read/{board_type}/{bno}")
    @Override
    public BoardDTO read(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        log.info(result);
        if(result.isPresent()){
            Board readResult = result.get();
            return entityToDTO(readResult);
        }
        return null;
    }

    // 특정 게시판의 목록("/board/{board_type}/list")
    @Override
    public BoardListResponseDTO<BoardDTO> getList(BoardListRequestDTO boardListRequestDTO, String board_type) {
        log.info(boardListRequestDTO + " : " + board_type);
        Page<Board> list = boardRepository.getBoardList(
                boardListRequestDTO.getType(),
                boardListRequestDTO.getKeyword(),
                boardListRequestDTO.getPageable()
        );
//        List<Object> collect = list.get().map().collect(Collectors.toList());

        BoardListResponseDTO boardListResponseDTO = BoardListResponseDTO.builder()
                .pageMaker(new PageMaker(boardListRequestDTO.getPage(), boardListRequestDTO.getSize(), (int) list.getTotalElements()))
                .boardDtoList(Collections.singletonList(list.toList()))
                .boardListRequestDTO(boardListRequestDTO)
                .build();

        return boardListResponseDTO;
    }

}

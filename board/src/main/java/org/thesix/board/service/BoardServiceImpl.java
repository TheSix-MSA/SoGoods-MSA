package org.thesix.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.entity.Board;
import org.thesix.board.repository.BoardRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // 게시판 글등록("/board/register")
    @Override
    public Long register(BoardDTO dto) {
        Board board = dtoToEntity(dto);
        boardRepository.save(board);
        return board.getBno();
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

    // 게시판 특정 글조회("/board/read/{bno}")
    @Override
    public BoardDTO read(Long bno) {
        Optional<Board> result = boardRepository.findById(bno);
        log.info(result);
        if(result.isPresent()){
            Board board = result.get();
            return entityToDTO(board);
        }
        return null;
    }
}

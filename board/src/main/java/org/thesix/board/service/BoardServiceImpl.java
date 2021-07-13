package org.thesix.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thesix.board.dto.BoardDTO;
import org.thesix.board.dto.BoardListDTO;
import org.thesix.board.entity.Board;
import org.thesix.board.repository.BoardRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    // 게시판 글등록("/board/{type}/register")
    @Override
    public BoardDTO register(BoardDTO dto) {
        Board register = dtoToEntity(dto);
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

    // 게시판 특정 글조회("/board/read/{type}/{bno}")
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

    @Override
    public BoardDTO boardList(String type) {
//        String searchType = type;
//        List<Object[]> searchResult = boardRepository.getBoardList(searchType);
//        for (Object[] arr : searchResult) {
//            System.out.println(Arrays.toString(arr));
//        }
        return null;
    }

}

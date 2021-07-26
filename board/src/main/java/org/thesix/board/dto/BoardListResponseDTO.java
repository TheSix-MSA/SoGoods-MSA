package org.thesix.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.thesix.board.entity.Board;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponseDTO<DTO> {
    private BoardListRequestDTO boardListRequestDTO;
    private List<DTO> boardDtoList;
    private PageMaker pageMaker;
}

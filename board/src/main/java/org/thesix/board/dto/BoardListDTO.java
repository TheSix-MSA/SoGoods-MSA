// 게시판 목록 비닐봉지
package org.thesix.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDTO<B> {

    private List<B> listDTO;
}

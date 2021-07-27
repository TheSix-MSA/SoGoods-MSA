// 게시판 목록 비닐봉지
package org.thesix.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.thesix.board.entity.BoardType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 12;
    private String keyword;
    private String type;

    @JsonIgnore
    public Pageable getPageable() {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("bno").descending());
        return pageable;
    }
}

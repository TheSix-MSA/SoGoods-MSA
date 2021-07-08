package org.thesix.funding.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRequestDTO {

    @Builder.Default // Builder로 값을 넣을 때 값이 없으면 default값이 들어감
    private int page = 1;
    @Builder.Default
    private int size = 10;

    private String keyword;

    // 파라미터로 들어오는 page, size 값이 음수일 경우 디폴트값으로 정의
    public void setPage(int page) {

        this.page = page > 0 ? page : 1;
    }

    public void setSize(int size) {

        this.size = size < 10 ? size : 10;
    }

    @JsonIgnore
    public Pageable getPageable() {

        return PageRequest.of(this.page - 1, this.size);
    }
}

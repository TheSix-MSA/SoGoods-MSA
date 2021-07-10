package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageMaker {

    private long page;
    private long size;
    private String keyword;
    private String type;
    private List<Integer> pageList;
    private int startPage;
    private int endPage;
    private boolean prev,next;


    public PageMaker(Pageable pageable,RequestListDTO dto, int totalCount){
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize() < 10 ? 10 : pageable.getPageSize();
        this.keyword = dto.getKeyword();
        this.type = dto.getType();

        int totalPage = (int)(Math.ceil(totalCount/(double)size));

        int tempEnd = (int)Math.ceil(page / 10.0) * 10;
        this.startPage = tempEnd-9;
        this.endPage = tempEnd < totalPage ? tempEnd : totalPage;

        prev = startPage > 1;
        next = tempEnd <  totalPage;

        pageList = IntStream.rangeClosed(startPage,endPage).boxed().collect(Collectors.toList());

    }


}

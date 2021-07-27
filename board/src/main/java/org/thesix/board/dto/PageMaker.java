package org.thesix.board.dto;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageMaker {
    private int page;
    private int size;
    private int totalCount;
    private List<Integer> pageList;
    private boolean prev,next;
    private int start;
    private int end;

    public PageMaker(Pageable pageable, int totalCount){
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize() < 12 ? 12 : pageable.getPageSize();
        this.totalCount = totalCount;
        int totalPage = (int)(Math.ceil(totalCount/(double)size));
        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd: totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }

}

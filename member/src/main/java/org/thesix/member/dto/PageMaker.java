package org.thesix.member.dto;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageMaker {

    private long page;
    private long size;
    private String keyword;
    private String type;
    private List<Integer> pageList;
    private int startPage;
    private int endPage;
    private boolean prev,next;


    public void PageMaker(Pageable pageable,RequestListDTO dto, int totalCount){
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize() < 10 ? 10 : pageable.getPageSize();
        this.keyword = dto.getKeyword();
        this.type = dto.getType();

        int totalPage = (int)(Math.ceil(totalCount/(double)size));

        int tempEnd = (int)Math.ceil(page / 10.0) * 10;
        this.startPage = tempEnd-10;
        this.endPage = tempEnd < totalPage ? tempEnd : totalPage;

        prev = startPage > 1;
        next = tempEnd <  totalPage;

        pageList = IntStream.rangeClosed(startPage,endPage).boxed().collect(Collectors.toList());

    }


}

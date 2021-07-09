package org.thesix.board.dto;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageMaker {

    // 페이지 수
    private int page;

    // 페이지 내에 데이터 개수
    private int size;

    // 전체 데이터 양
    private int totalCount;

    // 페이지의 목록
    private List<Integer> pageList;

    // 이전, 다음
    private boolean prev, next;

    public PageMaker(int page, int size, int totalCount) {
        this.page = page;
        this.size= size;
        this.totalCount = totalCount;

        int totalPage = (int)(Math.ceil(totalCount/(double)size));
        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        int start = tempEnd -9;
        prev = start > 1;
        int end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}

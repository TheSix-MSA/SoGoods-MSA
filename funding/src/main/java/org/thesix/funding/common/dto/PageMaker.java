package org.thesix.funding.common.dto;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageMaker {

    private int page;  // 페이지 번호
    private int size;  // 페이징 사이즈
    private int totalCount;  // 전체 데이터 수
    private List<Integer> pageList;  // 페이지 리스트
    private boolean prev, next;  // 이전페이지, 다음페이지가 있는지


    public PageMaker(int page, int size, int totalCount) {
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        // 전체페이지 = 전체 데이터 수 / 페이징 사이즈
        int totalPage = (int) (Math.ceil(totalCount / (double) size));
        // 임시 끝 페이지 번호
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        int start = tempEnd - 9;

        prev = start > 1;

        int end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }


}

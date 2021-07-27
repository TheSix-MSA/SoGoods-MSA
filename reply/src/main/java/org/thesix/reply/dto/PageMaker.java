package org.thesix.reply.dto;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//현재 페이지 번호
//사이즈
//전체 개수
@Data
public class PageMaker {
    private int page;
    private int size;
    private int totalCount;
    private List<Integer> pageList;
    private boolean prev,next;

    public PageMaker(int page,int size, int totalCount){
        /**
         * 강사님과 함께한 페이 정보 만드는 클래스
         */
        this.page = page;
        this.size = size;
        this.totalCount = totalCount;

        int totalPage = (int)(Math.ceil(totalCount/(double)size));
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        int start = tempEnd - 9;

        prev = start > 1;

        int end = totalPage > tempEnd ? tempEnd: totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
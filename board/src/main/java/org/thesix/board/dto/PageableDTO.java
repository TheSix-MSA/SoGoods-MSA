package org.thesix.board.dto;

import lombok.Builder;

public class PageableDTO {
    // 기본페이지
    @Builder.Default
    private int page = 1;

    // 페이지에 보여질 데이터 수
    @Builder.Default
    private int size = 10;

    // 검색어
    private String keyword;

    // 페이지가 0보다 작으면 1, 아니면 페이지 표시
    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    // 데이터의 양이 10보다 작으면 10까지 아니면 데이터양까지
    public void setSize(int size) {
        this.size = size < 10 ? 10 : size;
    }
}

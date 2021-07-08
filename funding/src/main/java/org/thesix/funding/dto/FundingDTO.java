package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingDTO {

    private Long fno;  // 펀딩 식별번호

    private String title;  // 펀딩글 제목

    private String writer;  // 펀딩글 작성자

    private String email;  // 작성자 이메일

    private String content;  // 펀딩글 내용

    private LocalDateTime regDate; // 펀딩글 등록일

    private LocalDateTime dueDate;  // 펀딩 만료일

    private boolean success;  // 펀딩 성공여부

    private boolean removed;  // 펀딩 삭제여부
}

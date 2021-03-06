package org.thesix.funding.dto.funding;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Lob;
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

    @Lob
    private String content;  // 펀딩글 내용

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate; // 펀딩글 등록일

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modDate; // 펀딩글 수정일

    private String dueDate;  // 펀딩 만료일

    private boolean success;  // 펀딩 성공여부

    private boolean removed;  // 펀딩 삭제여부

    private long totalAmount; // 총 모금액

    private long targetAmount; // 목표 금액

    private boolean authorized;  // 관리자 승인 여부

    private boolean requestApproval; // 승인 신청 여부

}

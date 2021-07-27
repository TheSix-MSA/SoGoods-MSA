package org.thesix.funding.dto.funding;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno; // 제품 식별번호

    private String name;  // 제품 이름

    private String des;  // 제품 설명

    private int price;  // 제품 가격

    private Long fno; // 펀딩 식별번호

    private boolean removed; // 삭제 여부

    private boolean deletable;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate; // 제품 등록일

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modDate; // 제품 수정일
}

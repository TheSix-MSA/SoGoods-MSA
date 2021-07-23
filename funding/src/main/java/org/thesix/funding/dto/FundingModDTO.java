package org.thesix.funding.dto;

import lombok.Data;

import javax.persistence.Lob;
import java.util.List;

@Data
public class FundingModDTO {

    private String title;  // 펀딩글 제목

    private String writer;  // 펀딩글 작성자

    private String email;  // 작성자 이메일

    @Lob
    private String content;  // 펀딩글 내용

    private List<ProductDTO> toBeDeletedDTO;

    private List<ProductDTO> toBeAddedDTO;



}

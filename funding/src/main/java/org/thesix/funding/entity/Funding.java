package org.thesix.funding.entity;

import lombok.*;
import org.thesix.funding.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name ="tbl_funding")
public class Funding extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;  // 펀딩 식별번호

    private String title;  // 펀딩글 제목

    private String writer;  // 펀딩글 작성자

    private String email;  // 작성자 이메일

    private String content;  // 펀딩글 내용

    private LocalDateTime dueDate;  // 펀딩 만료일

    private boolean success;  // 펀딩 성공여부

    private boolean removed;  // 펀딩 삭제여부

}

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
@Table(name = "tbl_funding")
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

    private long totalAmount; // 토탈 모금 금액

    private long targetAmount; // 목표 금액


    /**
     * 제목, 콘텐츠, 마감날짜, 삭제여부를 수정하는 메서드 : Setter 역할
     * @param title
     */
    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeDueDate(LocalDateTime dueDate){
        this.dueDate = dueDate;
    }

    public void changeTotalAmount(Long totalAmount){this.totalAmount = totalAmount;}

    public void changeRemoved(boolean removed){
        this.removed = removed;
    }


}

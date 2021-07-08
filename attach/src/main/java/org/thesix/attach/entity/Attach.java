package org.thesix.attach.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class Attach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    //파일명
    private String originalName;

    //uuid
    private String uuid;

    //대표이미지 여부
    private boolean main;

    //등록일
    private LocalDateTime regDate;

    private String tableName;

    private Long keyValue;


}

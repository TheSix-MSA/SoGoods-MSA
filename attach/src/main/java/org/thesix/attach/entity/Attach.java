package org.thesix.attach.entity;

import lombok.*;

import javax.persistence.Entity;
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
    private Long ano;

    //파일명
    private String original_name;

    //uuid
    private String uuid;

    //대표이미지 여부
    private boolean main;

    //등록일
    private LocalDateTime regdate;

    private String tablename;

    private Long key_value;


}

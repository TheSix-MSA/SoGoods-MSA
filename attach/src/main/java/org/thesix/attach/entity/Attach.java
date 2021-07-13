package org.thesix.attach.entity;

import lombok.*;
import org.thesix.common.BaseEntity;

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
public class Attach extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    //파일명
    private String originalName;

    //uuid
    private String uuid;

    //대표이미지 여부
    private boolean main;

    private String tableName;

    private Long keyValue;


}

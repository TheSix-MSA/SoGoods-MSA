package org.thesix.attach.dto;

import lombok.*;
import org.thesix.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttachDTO {

    private Long ano;

    private String originalName;

    private String uuid;

    private boolean main;

    private String tableName;

    private String keyValue;
}

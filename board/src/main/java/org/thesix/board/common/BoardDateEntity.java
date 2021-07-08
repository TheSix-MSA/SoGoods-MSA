package org.thesix.board.common;

import lombok.Getter;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// 게시판 관련 등록일 수정일 공통모듈

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
public class BoardDateEntity {

    // 글 등록일
    @CreatedDate
    @Column(name="regdate", updatable = false)
    private LocalDateTime regDate;

    // 글 수정일
    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
}

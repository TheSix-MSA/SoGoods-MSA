package org.thesix.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="tbl_refresh")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private String email;

    private long expireDate;
}

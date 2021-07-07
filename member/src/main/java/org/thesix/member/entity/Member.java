package org.thesix.member.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    private String detailAddress;

    @Column(nullable = false)
    private boolean removed;

    @Column(nullable = false)
    private boolean banned;

    private String provider;

    @Column(nullable = false)
    private boolean social;

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @CreatedDate
    @Column(name="loginDate", updatable = false)
    private LocalDateTime loginDate;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole role) {
        roleSet.add(role);
    }


}

package org.thesix.member.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@ToString(exclude = "roleSet")
public class Member {

    @Id
    private String email; //이메일 (pk값)

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name; // 사용자 실명

    @Column(nullable = false)
    private String gender; // 사용자 성별

    @Column(nullable = false)
    private String birth; // 사용자 생년월일(6자리)

    @Column(nullable = false)
    private String phone; //사용자 전화번호

    @Column(nullable = false)
    private String address; //사용자 주소(세부주소를 제외한)

    private String detailAddress; //사용자 상세주소

    @Column(nullable = false)
    private boolean removed; //삭제여부 (0: 미삭제, 1: 삭제)

    @Column(nullable = false)
    private boolean banned; // 차단여부 (0: 미차단, 1: 차단)

    private String provider; // 소셜로그인시 공급자 사이트

    @Column(nullable = false)
    private boolean social; // 소셜로그인 여부 (0: 일반로그인, 1: 소셜로그인)

    @CreationTimestamp
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @CreationTimestamp
    @Column(name="loginDate", updatable = false)
    private LocalDateTime loginDate;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole role) {
        roleSet.add(role);
    }

    public void changeMemberInfo(Member member){
        this.phone = member.getPhone();
        this.address = member.getAddress();
        this.detailAddress = member.getDetailAddress();
    }

    public void changeRemoved(boolean removed) {
        this.removed = removed;
    }

    public void changeBanned(boolean banned) {
        this.banned = banned;
    }

    public void changeLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }
}

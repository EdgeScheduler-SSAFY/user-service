package com.ssafy.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "member")
@ToString(of = {"id", "department", "zoneId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Integer id;

    @Builder.Default
    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer profile = 1;
    //    @Column(nullable = false, length = 50, unique = true)
    private String email;
    private String department;
    //    @Column(nullable = false, columnDefinition = "INT DEFAULT Asia/Seoul")
    private String zoneId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Auth auth;

    public void changeProfile(Integer profile) {
        this.profile = profile;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public void changeDepartment(String department) {
        this.department = department;
    }

}

package com.ssafy.userservice.entity;

import com.ssafy.userservice.dto.AuthDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@SQLRestriction("is_deleted = false")
@ToString(of = {"id", "username", "password", "refreshToken", "member", "lastLoginDate"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Setter
    private String refreshToken;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted;

    @Column(columnDefinition = "DATE")
    private LocalDate lastLoginDate;

    @Setter
    @OneToOne(mappedBy = "auth", cascade = CascadeType.ALL)
    private Member member;

    public static Auth toDto(AuthDto authDto) {
        return Auth.builder()
            .id(authDto.getId())
            .username(authDto.getUsername())
            .password(authDto.getPassword())
            .name(authDto.getName())
            .lastLoginDate(authDto.getLastLoginDate())
            .role(authDto.getRole())
            .provider(authDto.getProvider())
            .providerId(authDto.getProviderId())
            .refreshToken(authDto.getRefreshToken())
            .build();
    }

    public void deleteRefreshToken() {
        this.refreshToken = null;
    }

    public void deactivate() {
        this.isDeleted = true;
    }

    public void activate() {
        this.isDeleted = false;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void setLastLoginDate() {
        this.lastLoginDate = LocalDate.now();
    }

}

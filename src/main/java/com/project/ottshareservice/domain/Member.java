package com.project.ottshareservice.domain;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private boolean emailChecked = false;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String role;

    private int point;

    @Lob
    private String description;

    private boolean notificationByEmail;

    private boolean notificationByWeb = true;

    private boolean keywordNotificationByEmail;

    private boolean keywordNotificationByWeb = true;

    @OneToMany(mappedBy = "master")
    private List<Share> shares = new ArrayList<>();

    @ManyToMany
    private List<Share> joinedShares = new ArrayList<>();

    @ManyToMany
    private Set<Keyword> keywords = new HashSet<>();

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public boolean canResendEmailCheck() {
        return this.emailCheckTokenGeneratedAt.isAfter(LocalDateTime.now().minusMinutes(1));
    }

    public boolean isEmailCheckTimeout() {
        return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusMinutes(3));
    }

    public void completeCheck() {
        this.emailChecked = true;
        this.role = "ROLE_CHECKED_USER";
    }

    public void plusPoint(Long point) {
        this.point += point;
    }
}

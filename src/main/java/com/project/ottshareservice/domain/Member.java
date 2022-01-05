package com.project.ottshareservice.domain;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private Long id;

    @Column(unique = true)
    private String email;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedAt;

    private boolean emailChecked = false;

    @Column(unique = true)
    private String nickname;

    private String role;

    @NumberFormat(pattern = "###,###")
    private int point;


    private String password;

    @Lob
    private String description;

    private boolean notificationByEmail;

    private boolean notificationByWeb = true;

    private boolean keywordNotificationByEmail;

    private boolean keywordNotificationByWeb = true;

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

    public void completeSignUp() {
        this.emailChecked = true;
    }
}

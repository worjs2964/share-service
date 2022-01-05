package com.project.ottshareservice.member;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.mail.EmailMessage;
import com.project.ottshareservice.mail.MailSender;
import com.project.ottshareservice.member.form.SignUpForm;
import com.project.ottshareservice.settings.form.MemberUpdateForm;
import com.project.ottshareservice.settings.form.NicknameForm;
import com.project.ottshareservice.settings.form.NotificationsForm;
import com.project.ottshareservice.settings.form.PasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(useremail);
        if (member == null) {
            throw new UsernameNotFoundException(useremail);
        }

        return new UserAccount(member);
    }

    public Member signUp(SignUpForm signUpForm) {
        Member member = saveMember(signUpForm);
        sendEmailCheckToken(member);
        return member;
    }

    private Member saveMember(SignUpForm signUpForm) {
        Member member = formToMember(signUpForm);
        member.encodePassword(passwordEncoder);
        member.generateEmailCheckToken();
        return memberRepository.save(member);
    }

    private Member formToMember(SignUpForm signUpForm) {
        Member newMember = Member.builder()
                .nickname(signUpForm.getNickname())
                .email(signUpForm.getEmail())
                .notificationByWeb(true)
                .keywordNotificationByWeb(true)
                .emailChecked(false)
                .password(signUpForm.getPassword())
                .role("USER")
                .build();
        return newMember;
    }

    private void sendEmailCheckToken(Member member) {
        Context context = new Context();
        context.setVariable("nickname", member.getNickname());
        context.setVariable("link", "check-email-token?token=" + member.getEmailCheckToken() + "&email=" + member.getEmail());
        String message = templateEngine.process("mail/check-email-form", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .title("ott-share-service 이메일 인증")
                .message(message).build();

        mailSender.send(emailMessage);
    }

    public void resendCheckEmail(Member member) {
        member.generateEmailCheckToken();
        memberRepository.save(member);
        sendEmailCheckToken(member);
    }

    public void signIn(Member member) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(member),
                member.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(token);

    }

    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public void updateMember(Member member, MemberUpdateForm memberUpdateForm) {
        member.setDescription(memberUpdateForm.getDescription());
        memberRepository.save(member);
    }

    public void updatePassword(Member member, String newPassword) {
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    public void updateNotifications(Member member, NotificationsForm notificationsForm) {
        member.setNotificationByEmail(notificationsForm.isNotificationByEmail());
        member.setNotificationByWeb(notificationsForm.isNotificationByWeb());
        member.setKeywordNotificationByEmail(notificationsForm.isKeywordNotificationByEmail());
        member.setKeywordNotificationByWeb(notificationsForm.isKeywordNotificationByWeb());
        memberRepository.save(member);
    }

    public void updateNickname(Member member, NicknameForm nicknameForm) {
        member.setNickname(nicknameForm.getNickname());
        memberRepository.save(member);

    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public void completeSignUp(Member member) {
        member.completeSignUp();
        signIn(member);
    }
}

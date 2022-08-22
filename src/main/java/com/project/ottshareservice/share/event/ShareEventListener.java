package com.project.ottshareservice.share.event;

import com.project.ottshareservice.domain.*;
import com.project.ottshareservice.mail.EmailMessage;
import com.project.ottshareservice.mail.MailSender;
import com.project.ottshareservice.member.MemberRepository;
import com.project.ottshareservice.notification.NotificationRepository;
import com.project.ottshareservice.share.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.ottshareservice.domain.QMember.member;

@Async
@Component
@RequiredArgsConstructor
@Transactional
public class ShareEventListener {

    private final ShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final MailSender mailSender;
    private final TemplateEngine templateEngine;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void publishShare(ShareCreateEvent shareCreateEvent) {
        Share share = shareRepository.findWithKeywordById(shareCreateEvent.getShare().getId());
        Iterable<Member> members = memberRepository.findAll(member.keywords.any().in(share.getKeywords()));
        members.forEach(member -> {
            if (member.isKeywordNotificationByEmail()) {
                sendNotificationByMail(member, "쉐어 서비스(키워드 알림)", share.getTitle() + "생성",
                        "키워드에 해당하는 공유가 생성되었습니다.\n아래 링크를 눌러 확인해보세요.", "share/" + share.getId());
            }

            if (member.isKeywordNotificationByWeb()) {
                sendNotificationByWeb(member, share, "키워드 등록한 공유가 개설되었습니다.", NotificationType.CREATED, "/share/" + share.getId());
            }
        });
    }

    @EventListener
    public void updateShare(ShareUpdateEvent shareUpdateEvent) {
        Share share = shareRepository.findById(shareUpdateEvent.getShare().getId()).get();
        List<Member> members = share.getMembers();
        members.forEach(member -> {
            if (member.isNotificationByEmail()) {
                sendNotificationByMail(member, "쉐어 서비스(공유 업데이트 알림)", share.getTitle() + "의 내용이 수정되었습니다.",
                        "해당 공유의 정보가 수정되었습니다.\n아래 링크를 눌러 확인해보세요.", "share/" + share.getId() +"/info");
            }

            if (member.isNotificationByWeb()) {
                sendNotificationByWeb(member, share, "가입하신 공유의 계정 정보가 변경되었습니다.", NotificationType.JOINED, "/share/" + share.getId() + "/info");
            }
        });
    }

    @EventListener
    public void joinShare(ShareJoinEvent shareJoinEvent) {
        Member member = memberRepository.findById(shareJoinEvent.getMember().getId()).get();
        Share share = shareRepository.findById(shareJoinEvent.getShare().getId()).get();

        sendJoinInfoToMember(member, share);
        sendJoinInfoToMaster(share.getMaster(), share);
    }

    private void sendJoinInfoToMember(Member member, Share share) {
        if (member.isNotificationByEmail()) {
            sendNotificationByMail(member, "쉐어 서비스(공유 참여 알림)", share.getTitle() + "에 가입하였습니다.",
                    "공유 계정: " + share.getShareEmail() + ", 비밀번호: " + share.getSharePassword(), "share/" + share.getId());
        }

        if (member.isNotificationByWeb()) {
            sendNotificationByWeb(member, share, "새로운 공유에 참여하셨습니다.", NotificationType.JOINED, "/share/" + share.getId());
        }
    }

    private void sendJoinInfoToMaster(Member master, Share share) {
        if (master.isNotificationByEmail()) {
            sendNotificationByMail(master, "쉐어 서비스(공유 참여 알림)", share.getTitle() + "에 회원이 참여하였습니다.",
                    "해당 공유에 회원이 참여하였습니다. 링크를 눌러 확인해보세요.", "share/" + share.getId());
        }

        if (master.isNotificationByWeb()) {
            sendNotificationByWeb(master, share, "새로운 회원이 공유에 참여하였습니다.", NotificationType.JOINED, "/share/" + share.getId());
        }
    }

    private void sendNotificationByMail(Member member, String mailTitle, String contentTitle, String text, String link) {
        Context context = new Context();
        context.setVariable("title", contentTitle);
        context.setVariable("nickname", member.getNickname());
        context.setVariable("message", text);
        context.setVariable("link", link);
        String message = templateEngine.process("mail/email-form", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .title(mailTitle)
                .message(message).build();

        mailSender.send(emailMessage);
    }


    private void sendNotificationByWeb(Member member, Share share, String message, NotificationType notificationType, String link) {
        Notification notification = new Notification();
        notification.setTitle(share.getTitle());
        notification.setLink(link);
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setMessage(message);
        notification.setMember(member);
        notification.setNotificationType(notificationType);
        notificationRepository.save(notification);
    }
}

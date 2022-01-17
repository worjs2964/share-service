package com.project.ottshareservice.notification;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Notification;
import com.project.ottshareservice.mail.MailSender;
import com.project.ottshareservice.member.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String viewNotification(@CurrentMember Member member, Model model) {
        List<Notification> notifications = notificationRepository.findByMemberAndCheckedOrderByCreatedDateTimeDesc(member, false);
        Long numberOfChecked = notificationRepository.countByMemberAndChecked(member, true);
        putCategorizedNotifications(model, notifications, numberOfChecked, notifications.size());
        model.addAttribute("isNew", true);
        notificationService.readNotifications(notifications);
        return "notification/view";
    }

    @GetMapping("/notifications/old")
    public String viewOldNotification(@CurrentMember Member member, Model model) {
        List<Notification> notifications = notificationRepository.findByMemberAndCheckedOrderByCreatedDateTimeDesc(member, true);
        Long numberOfNotChecked = notificationRepository.countByMemberAndChecked(member, false);
        putCategorizedNotifications(model, notifications, notifications.size(), numberOfNotChecked);
        model.addAttribute("isNew", false);
        return "notification/view";
    }

    @DeleteMapping("/notifications")
    public String deleteNotfications(@CurrentMember Member member) {
        notificationService.deleteNotifications(member);
        return "redirect:/notifications";
    }


    private void putCategorizedNotifications(Model model, List<Notification> notifications, long numberOfChecked, long numberOfNotChecked) {
        List<Notification> newShareNotifications = new ArrayList<>();
        List<Notification> joinedShareNotifications = new ArrayList<>();
        for (var notification : notifications) {
            switch (notification.getNotificationType()) {
                case CREATED:
                    newShareNotifications.add(notification);
                    break;
                case JOINED:
                    joinedShareNotifications.add(notification);
                    break;
            }
        }
        model.addAttribute("numberOfNotChecked", numberOfNotChecked);
        model.addAttribute("numberOfChecked", numberOfChecked);
        model.addAttribute("notifications", notifications);
        model.addAttribute("newShareNotifications", newShareNotifications);
        model.addAttribute("joinedShareNotifications", joinedShareNotifications);
    }
}

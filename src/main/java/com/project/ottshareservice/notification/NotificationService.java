package com.project.ottshareservice.notification;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void readNotifications(List<Notification> notifications) {
        notifications.forEach(notification -> {
            notification.setChecked(true);
        });
    }

    public void deleteNotifications(Member member) {
        notificationRepository.deleteByMember(member);

    }
}

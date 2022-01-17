package com.project.ottshareservice.notification;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void findByMember(Member member);

    List<Notification> findByMemberAndCheckedOrderByCreatedDateTimeDesc(Member member, boolean b);

    Long countByMemberAndChecked(Member member, boolean b);

    List<Notification> findAllByMember(Member member);

    void deleteByMember(Member member);
}

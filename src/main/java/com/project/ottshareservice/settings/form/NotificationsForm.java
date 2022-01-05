package com.project.ottshareservice.settings.form;

import com.project.ottshareservice.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationsForm {

    private boolean notificationByEmail;

    private boolean notificationByWeb;

    private boolean keywordNotificationByEmail;

    private boolean keywordNotificationByWeb;

    public NotificationsForm(Member member) {
        this.notificationByEmail = member.isNotificationByEmail();
        this.notificationByWeb = member.isNotificationByWeb();
        this.keywordNotificationByEmail = member.isKeywordNotificationByEmail();
        this.keywordNotificationByWeb = member.isKeywordNotificationByWeb();
    }
}

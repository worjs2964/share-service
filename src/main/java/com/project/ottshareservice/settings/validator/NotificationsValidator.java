package com.project.ottshareservice.settings.validator;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.settings.form.NotificationsForm;
import com.project.ottshareservice.settings.form.PasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NotificationsValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(NotificationsForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validate(Object object, Member member, Errors errors) {
        NotificationsForm notificationsForm = (NotificationsForm) object;

        if (!member.isEmailChecked() && notificationsForm.isNotificationByEmail()) {
            errors.rejectValue("notificationByEmail", "invalid.notificationByEmail", "이메일 인증을 완료하지 않으면 이메일 알림을 사용할 수 없습니다.");
        }
        if (!member.isEmailChecked() && notificationsForm.isKeywordNotificationByEmail()) {
            errors.rejectValue("keywordNotificationByEmail", "invalid.keywordNotificationByEmail", "이메일 인증을 완료하지 않으면 이메일 알림을 사용할 수 없습니다.");
        }
    }
}

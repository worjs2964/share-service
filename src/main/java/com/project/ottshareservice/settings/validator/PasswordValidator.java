package com.project.ottshareservice.settings.validator;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.MemberRepository;
import com.project.ottshareservice.member.form.SignUpForm;
import com.project.ottshareservice.settings.form.PasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements Validator {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validate(Object object, Member member, Errors errors) {
        PasswordForm passwordForm = (PasswordForm)object;

        if (!passwordEncoder.matches(passwordForm.getPassword(), member.getPassword())) {
            errors.rejectValue("password", "invalid.password", "패스워드가 틀립니다.");
        } else if (!passwordForm.getNewPassword().equals(passwordForm.getCheckPassword())) {
            errors.rejectValue("checkPassword", "invalid.checkPassword", "새 패스워드가 서로 다릅니다.");
        }

    }
}

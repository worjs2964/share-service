package com.project.ottshareservice.settings.validator;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.MemberRepository;
import com.project.ottshareservice.settings.form.NicknameForm;
import com.project.ottshareservice.settings.form.PasswordForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(NicknameForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) object;

        if (memberRepository.existsByNickname(nicknameForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", "닉네임이 이미 존재합니다.");
        }
    }
}

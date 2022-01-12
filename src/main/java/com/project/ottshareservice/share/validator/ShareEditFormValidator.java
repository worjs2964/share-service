package com.project.ottshareservice.member.validator;

import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.member.MemberRepository;
import com.project.ottshareservice.member.form.SignUpForm;
import com.project.ottshareservice.share.ShareRepository;
import com.project.ottshareservice.share.form.ShareForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ShareEditFormValidator implements Validator {

    private final ShareRepository shareRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(ShareForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ShareForm shareForm = (ShareForm) object;
        Share share = shareRepository.findById(shareForm.getId()).get();

        if (share.getJoinMemberCount() > shareForm.getRecruitmentCount()) {
            errors.rejectValue("recruitment", "invalid.recruitment", "모집인원은 이미 참여중인 회원보다 적을 수 없습니다.");
        }

        if (!share.getMembers().isEmpty() && !share.getShareFinishAt().equals(shareForm.getShareFinishAt())) {
            errors.rejectValue("shareFinishAt", "invalid.shareFinishAt", "참여중인 회원이 있을때는 날짜를 변경할 수 없습니다.");
        }

    }
}

package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.share.form.ShareForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShareService {

    private final ShareRepository shareRepository;

    public Share save(ShareForm shareForm, Member member) {
        Share share = formToShare(shareForm, member);
        return shareRepository.save(share);
    }

    private Share formToShare(ShareForm shareForm, Member member) {
        return Share.builder()
                .title(shareForm.getTitle())
                .serviceName(shareForm.getServiceName())
                .shareEmail(shareForm.getShareEmail())
                .sharePassword(shareForm.getSharePassword())
                .recruitmentCount(shareForm.getRecruitmentCount())
                .dailyRate(shareForm.getDailyRate())
                .contentType(shareForm.getContentType())
                .description(shareForm.getDescription())
                .shareFinishAt(shareForm.getShareFinishAt())
                .master(member).build();
    }
}

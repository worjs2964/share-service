package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.Keyword;
import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.share.form.ShareForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

    public void editShare(Share share, ShareForm shareForm) {
        share.setTitle(shareForm.getTitle());
        share.setServiceName(shareForm.getServiceName());
        share.setShareEmail(shareForm.getShareEmail());
        share.setSharePassword(shareForm.getSharePassword());
        share.setShareFinishAt(shareForm.getShareFinishAt());
        share.setContentType(shareForm.getContentType());
        share.setDescription(shareForm.getDescription());
        share.setRecruitmentCount(shareForm.getRecruitmentCount());
        share.setDailyRate(shareForm.getDailyRate());
    }

    public void join(Share share, Member member) {
        share.join(member);
    }

    public Boolean changeRecruiting(Share share, boolean recruiting) {
        share.setRecruiting(recruiting);
        return share.isRecruiting();
    }

    public void addKeyword(Share share, Keyword keyword) {
        share.getKeywords().add(keyword);
    }

    public void removeKeyword(Share share, Keyword keyword) {
        share.getKeywords().remove(keyword);
    }
}

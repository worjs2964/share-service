package com.project.ottshareservice.home.dto;

import com.project.ottshareservice.domain.Share;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ShareCardDto {

    private String serviceName;

    private String title;

    private Long joinedMemberCount;

    private Long remainMemberCount;

    private LocalDate shareFinishAt;

    private Long remainDays;

    private Long cost;

    public ShareCardDto(Share share) {
        this.serviceName = share.getServiceName();
        this.title = share.getTitle();
        this.joinedMemberCount = share.getJoinMemberCount();
        this.remainMemberCount = share.getRecruitmentCount() - share.getJoinMemberCount();
        this.shareFinishAt = share.getShareFinishAt();
        this.remainDays = share.getRamainDays();
        this.cost = share.getCost();
    }
}

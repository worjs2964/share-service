package com.project.ottshareservice.share.event;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShareJoinEvent {

    private final Share share;

    private final Member member;
}

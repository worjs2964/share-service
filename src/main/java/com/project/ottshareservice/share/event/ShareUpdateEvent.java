package com.project.ottshareservice.share.event;

import com.project.ottshareservice.domain.Share;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShareUpdateEvent {

    private final Share share;
}

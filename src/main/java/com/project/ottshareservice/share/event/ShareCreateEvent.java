package com.project.ottshareservice.share.event;

import com.project.ottshareservice.domain.Share;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class ShareCreateEvent{

    private final Share share;
}

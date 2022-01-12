package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.member.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final ShareService shareService;
    private final ShareRepository shareRepository;

    @GetMapping("/complete/{shareId}")
    public String joinShare(@CurrentMember Member member, @PathVariable Long shareId) {
        Share share = shareRepository.findById(shareId).get();
        shareService.join(share, member);

        return "redirect:/share/"+shareId;
    }
}

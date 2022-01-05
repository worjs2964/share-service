package com.project.ottshareservice.home;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

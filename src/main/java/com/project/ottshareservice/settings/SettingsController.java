package com.project.settings;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.CurrentMember;
import com.project.settings.form.MemberUpdateForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        model.addAttribute("memberUpdateForm", new MemberUpdateForm(member));
        System.out.println("머지??");

        return "settings/profile";
    }
}

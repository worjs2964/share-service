package com.project.ottshareservice.share;

import com.project.ottshareservice.domain.ContentType;
import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.CurrentMember;
import com.project.ottshareservice.share.form.ShareForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;


    @ModelAttribute("contentTypes")
    public ContentType[] types() {
        return ContentType.values();
    }

    @GetMapping("/new-share")
    private String createShareForm(@CurrentMember Member member, Model model) {
        model.addAttribute(new ShareForm());
        model.addAttribute(member);

        return "share/form";
    }

    @PostMapping("/new-share")
    private String createShare(@CurrentMember Member member, @Validated @ModelAttribute ShareForm shareForm, BindingResult bindingResult,
                               Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(member);
            model.addAttribute(shareForm);
            return "share/form";
        }

        shareService.save(shareForm, member);
        return "redirect:/";
    }
}

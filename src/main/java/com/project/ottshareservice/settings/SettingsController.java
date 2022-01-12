package com.project.ottshareservice.settings;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.CurrentMember;
import com.project.ottshareservice.member.MemberRepository;
import com.project.ottshareservice.member.MemberService;
import com.project.ottshareservice.settings.form.MemberUpdateForm;
import com.project.ottshareservice.settings.form.NicknameForm;
import com.project.ottshareservice.settings.form.NotificationsForm;
import com.project.ottshareservice.settings.form.PasswordForm;
import com.project.ottshareservice.settings.validator.NicknameValidator;
import com.project.ottshareservice.settings.validator.NotificationsValidator;
import com.project.ottshareservice.settings.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final MemberService memberService;
    private final PasswordValidator passwordValidator;
    private final NicknameValidator nicknameValidator;
    private final NotificationsValidator notificationsValidator;

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        model.addAttribute("memberUpdateForm", new MemberUpdateForm(member));

        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@CurrentMember Member member, @Validated @ModelAttribute MemberUpdateForm memberUpdateForm, BindingResult bindingResult,
                                Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("member", member);
            return "settings/profile";
        }

        memberService.updateMember(member, memberUpdateForm);
        redirectAttributes.addFlashAttribute("message", "프로필을 수정하였습니다");

        return "redirect:/settings/profile";
    }

    @GetMapping("/settings/password")
    public String passwordUpdateForm(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        model.addAttribute("passwordForm", new PasswordForm());
        return "settings/password";
    }

    @PostMapping("/settings/password")
    public String passwordUpdate(@CurrentMember Member member, @Validated @ModelAttribute PasswordForm passwordForm,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        passwordValidator.validate(passwordForm, member, bindingResult);
        if (bindingResult.hasErrors()) {
            return "settings/password";
        }

        memberService.updatePassword(member, passwordForm.getNewPassword());
        redirectAttributes.addFlashAttribute("message", "패스워드를 수정하였습니다.");
        return "redirect:/settings/password";
    }

    @GetMapping("/settings/notifications")
    public String notificationsUpdateForm(@CurrentMember Member member, Model model) {
        model.addAttribute(member);
        model.addAttribute("notificationsForm", new NotificationsForm(member));
        return "settings/notifications";
    }

    @PostMapping("/settings/notifications")
    public String notificationsUpdate(@CurrentMember Member member, @Validated @ModelAttribute NotificationsForm notificationsForm,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        notificationsValidator.validate(notificationsForm, member, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(member);
            return "settings/notifications";
        }

        memberService.updateNotifications(member, notificationsForm);
        redirectAttributes.addFlashAttribute("message", "알림을 수정하였습니다.");
        return "redirect:/settings/notifications";
    }

    @GetMapping("/settings/account")
    public String accountUpdateForm(@CurrentMember Member member, Model model) {
        model.addAttribute("nicknameForm", new NicknameForm(member));

        return "settings/account";
    }

    @PostMapping("/settings/account")
    public String accountUpdate(@CurrentMember Member member, @Validated @ModelAttribute NicknameForm nicknameForm,
                                BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        nicknameValidator.validate(nicknameForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "settings/account";
        }

        memberService.updateNickname(member, nicknameForm);
        memberService.signIn(member);
        redirectAttributes.addFlashAttribute("message", "닉네임을 수정하였습니다.");
        return "redirect:/settings/account";
    }

    @DeleteMapping("/settings/account")
    public String deleteMember(@CurrentMember Member member, RedirectAttributes redirectAttributes) {
        memberService.deleteMember(member);
        memberService.logout();
        redirectAttributes.addFlashAttribute("message", "정상적으로 회원탈퇴가 되었습니다. 이용해주셔서 감사합니다.");
        return "redirect:/";
    }
}

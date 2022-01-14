package com.project.ottshareservice.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ottshareservice.domain.Keyword;
import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.keyword.KeywordRepository;
import com.project.ottshareservice.keyword.KeywordService;
import com.project.ottshareservice.keyword.form.KeywordForm;
import com.project.ottshareservice.member.CurrentMember;
import com.project.ottshareservice.member.MemberService;
import com.project.ottshareservice.settings.form.MemberUpdateForm;
import com.project.ottshareservice.settings.form.NicknameForm;
import com.project.ottshareservice.settings.form.NotificationsForm;
import com.project.ottshareservice.settings.form.PasswordForm;
import com.project.ottshareservice.settings.validator.NicknameValidator;
import com.project.ottshareservice.settings.validator.NotificationsValidator;
import com.project.ottshareservice.settings.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final MemberService memberService;
    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;
    private final PasswordValidator passwordValidator;
    private final NicknameValidator nicknameValidator;
    private final NotificationsValidator notificationsValidator;
    private final ObjectMapper objectMapper;

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

    @GetMapping("/settings/keywords")
    public String viewKeywords(@CurrentMember Member member, Model model) throws JsonProcessingException {
        model.addAttribute(member);

        Set<Keyword> tags = memberService.getKeywords(member);
        model.addAttribute("keywords", tags.stream().map(Keyword::getKeyword).collect(Collectors.toList()));

        List<String> keywords = keywordRepository.findAll().stream().map(Keyword::getKeyword).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(keywords));

        return "settings/keywords";
    }

    @PostMapping("/settings/keywords/add")
    @ResponseBody
    public ResponseEntity addKeyword(@CurrentMember Member member, @RequestBody KeywordForm keywordForm) {
        Keyword keyword = keywordService.findOrCreateNew(keywordForm.getKeyword());
        memberService.addKeyword(member, keyword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/settings/keywords/remove")
    @ResponseBody
    public ResponseEntity deleteKeyword(@CurrentMember Member member, @RequestBody KeywordForm keywordForm) {
        Keyword keyword = keywordService.findOrCreateNew(keywordForm.getKeyword());
        memberService.removeKeyword(member, keyword);
        return ResponseEntity.ok().build();
    }
}

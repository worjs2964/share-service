package com.project.ottshareservice.member;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.form.CheckEmailForm;
import com.project.ottshareservice.member.form.SignUpForm;
import com.project.ottshareservice.member.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final SignUpFormValidator signUpFormValidator;

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "member/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute SignUpForm signUpForm, BindingResult bindingResult) {
        signUpFormValidator.validate(signUpForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "member/sign-up";
        }

        Member member = memberService.signUp(signUpForm);
        memberService.signIn(member);
        return "redirect:/check-email";
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentMember Member member, Model model) {
        model.addAttribute("member", member);
        return "member/check-email";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Member member = memberRepository.findByEmail(email);
        if (member == null || !member.getEmailCheckToken().equals(token)) {
            model.addAttribute("error", "인증 메일에 문제가 있습니다.");
            return "member/checked-email";
        } else if (member.isEmailCheckTimeout()) {
            model.addAttribute("error", "이메일 인증 시간이 초과되었습니다. (인증 제한시간 3분)");
            return "member/checked-email";
        }
        model.addAttribute("member", member);
        memberService.completeSignUp(member);
        return "member/checked-email";
    }

    @GetMapping("/resend-check-email")
    public String resendCheckEmail(@CurrentMember Member member, Model model) {
        if (member.canResendEmailCheck()) {
            model.addAttribute("error", "이메일 재전송은 1분이 경과해야 보낼 수 있습니다.");
            return "member/check-email";
        }
        memberService.resendCheckEmail(member);
        return "redirect:/";
    }


    @GetMapping("/profile/{nickname}")
    public String profile(@PathVariable String nickname, Model model, @CurrentMember Member currentMember) {
        Member member = memberRepository.findByNickname(nickname);
        if (member == null) {
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }

        model.addAttribute("member", member);
        model.addAttribute("equalsMember", member.equals(currentMember));
        return "member/profile";
    }
}
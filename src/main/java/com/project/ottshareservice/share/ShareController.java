package com.project.ottshareservice.share;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ottshareservice.domain.ContentType;
import com.project.ottshareservice.domain.Keyword;
import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.keyword.KeywordRepository;
import com.project.ottshareservice.keyword.KeywordService;
import com.project.ottshareservice.keyword.form.KeywordForm;
import com.project.ottshareservice.member.CurrentMember;
import com.project.ottshareservice.member.validator.ShareEditFormValidator;
import com.project.ottshareservice.share.form.RecruitingDto;
import com.project.ottshareservice.share.form.ShareForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
public class ShareController {

    private final ShareService shareService;
    private final ShareRepository shareRepository;
    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;
    private final ShareEditFormValidator shareEditFormValidator;
    private final ObjectMapper objectMapper;

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="잘못된 접근입니다.")
    public class UrlNotFoundException extends RuntimeException { }

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

        redirectAttributes.addFlashAttribute("message", "공유 키워드를 설정할 수 있습니다. 키워드로 검색이 가능하며, 공유를 처음으로 공개로 바꿀 시 " +
                "키워드를 등록한 회원에게 알림이 갑니다");
        Share share = shareService.save(shareForm, member);
        return "redirect:/share/" + share.getId().toString();
    }

    @GetMapping("/share/{shareId}")
    private String shareView(@CurrentMember Member member, @PathVariable Long shareId, Model model) throws JsonProcessingException {
        Share share = shareRepository.findById(shareId).get();
        if (!share.isRecruiting() && !share.checkAlreadyJoinMember(member) && !share.isMaster(member)) {
            throw new UrlNotFoundException();
        } else if (share.isMaster(member)) {
            Set<Keyword> tags = share.getKeywords();
            model.addAttribute("keywords", tags.stream().map(Keyword::getKeyword).collect(Collectors.toList()));

            List<String> keywords = keywordRepository.findAll().stream().map(Keyword::getKeyword).collect(Collectors.toList());
            model.addAttribute("whitelist", objectMapper.writeValueAsString(keywords));
        }
        model.addAttribute(share);

        return "share/view";
    }

    @GetMapping("/share/{shareId}/edit")
    private String shareEditForm(@CurrentMember Member member, @PathVariable Long shareId, Model model) {
        Share share = shareRepository.findById(shareId).get();
        checkMaster(member, share);

        model.addAttribute(new ShareForm((share)));
        model.addAttribute(share);
        model.addAttribute(member);

        return "share/edit-form";
    }

    @PostMapping("/share/{shareId}/edit")
    private String shareEdit(@CurrentMember Member member, @PathVariable Long shareId, @Validated @ModelAttribute ShareForm shareForm,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        Share share = shareRepository.findById(shareId).get();
        checkMaster(member, share);

        shareForm.setId(share.getId());
        shareEditFormValidator.validate(shareForm, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute(shareForm);
            model.addAttribute(member);
            return "share/edit-form";
        }

        redirectAttributes.addFlashAttribute("message", "수정을 완료했습니다.");
        shareService.editShare(share, shareForm);
        return "redirect:/share/" + shareId.toString();
    }

    @GetMapping("/share/{shareId}/payment")
    private String paymentView(@CurrentMember Member member, @PathVariable Long shareId, Model model) {
        Share share = shareRepository.findById(shareId).get();
        if (share.isMaster(member) || !share.isRecruiting() || share.checkAlreadyJoinMember(member)) {
            throw new UrlNotFoundException();
        }
        model.addAttribute(share);
        model.addAttribute(member);
        return "share/payment-view";
    }

    @GetMapping("/share/{shareId}/info")
    private String shareInfo(@CurrentMember Member member, @PathVariable Long shareId, Model model) {
        Share share = shareRepository.findById(shareId).get();
        if (!share.checkAlreadyJoinMember(member)) {
            throw new UrlNotFoundException();
        }
        model.addAttribute(share);
        model.addAttribute(member);
        return "share/info-view";
    }

    @PostMapping("/share/{shareId}/recruiting")
    @ResponseBody
    private Boolean changeRecruiting(@CurrentMember Member member, @PathVariable Long shareId,
                                     @RequestBody RecruitingDto recruitingDto) {
        Share share = shareRepository.findById(shareId).get();
        checkMaster(member, share);
        return shareService.changeRecruiting(share, recruitingDto.isRecruiting());
    }

    private void checkMaster(Member member, Share share) {
        if (!share.isMaster(member)) {
            throw new UrlNotFoundException();
        }
    }

    @PostMapping("/share/{shareId}/keywords/add")
    @ResponseBody
    public ResponseEntity addKeyword(@CurrentMember Member member, @PathVariable("shareId") Share share, @RequestBody KeywordForm keywordForm) {
        Keyword keyword = keywordService.findOrCreateNew(keywordForm.getKeyword());
        shareService.addKeyword(share, keyword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/share/{shareId}/keywords/remove")
    @ResponseBody
    public ResponseEntity deleteKeyword(@CurrentMember Member member, @PathVariable("shareId") Share share, @RequestBody KeywordForm keywordForm) {
        Keyword keyword = keywordService.findOrCreateNew(keywordForm.getKeyword());
        shareService.removeKeyword(share, keyword);
        return ResponseEntity.ok().build();
    }
}

package com.project.ottshareservice.home;

import com.project.ottshareservice.domain.ContentType;
import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.domain.Share;
import com.project.ottshareservice.home.dto.ShareCardDto;
import com.project.ottshareservice.member.CurrentMember;
import com.project.ottshareservice.share.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ShareRepository shareRepository;

    @GetMapping("/")
    public String index(@CurrentMember Member member, Model model) {
        List<Share> shares = shareRepository.findNew12();
        model.addAttribute("shares", shares);
        model.addAttribute("member", member);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/share")
    public String keywordSearch(String keyword, Model model,
                                @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Share> sharePage = shareRepository.findByKeyword(keyword, pageable);
        model.addAttribute("sharePage", sharePage);
        model.addAttribute("keyword", keyword);
        return "search/keyword";
    }

    @GetMapping("/search/video")
    public String videoSearch(Model model, @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Share> sharePage = shareRepository.findByType(ContentType.VIDEO, pageable);
        model.addAttribute("sharePage", sharePage);
        model.addAttribute("type", ContentType.VIDEO);
        return "search/share-type";
    }

    @GetMapping("/search/music")
    public String musicSearch(Model model, @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Share> sharePage = shareRepository.findByType(ContentType.MUSIC, pageable);
        model.addAttribute("sharePage", sharePage);
        model.addAttribute("type", ContentType.MUSIC);
        return "search/share-type";
    }

    @GetMapping("/search/game")
    public String gameSearch(Model model, @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Share> sharePage = shareRepository.findByType(ContentType.GAME, pageable);
        model.addAttribute("sharePage", sharePage);
        model.addAttribute("type", ContentType.GAME);
        return "search/share-type";
    }

    @GetMapping("/search/etc")
    public String etcSearch(Model model, @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Share> sharePage = shareRepository.findByType(ContentType.ETC, pageable);
        model.addAttribute("sharePage", sharePage);
        model.addAttribute("type", ContentType.ETC);
        return "search/share-type";
    }

}

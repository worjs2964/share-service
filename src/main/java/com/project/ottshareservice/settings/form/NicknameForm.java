package com.project.ottshareservice.settings.form;

import com.project.ottshareservice.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class NicknameForm {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,20}$", message = "형식또는 길이가 일치하지 않습니다.")
    private String nickname;

    public NicknameForm(Member member) {
        this.nickname = member.getNickname();
    }
}

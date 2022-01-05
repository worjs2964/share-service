package com.project.ottshareservice.member.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{2,20}$", message = "형식또는 길이가 일치하지 않습니다.")
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 8, max = 30, message = "비밀번호의 길이가 충족되지 않습니다.")
    private String password;

    @NotBlank
    @Length(min = 8, max = 30, message = "비밀번호의 길이가 충족되지 않습니다.")
    private String checkPassword;
}

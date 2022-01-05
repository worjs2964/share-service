package com.project.ottshareservice.settings.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordForm {

    @Length(min = 8, max = 30, message = "비밀번호의 길이가 충족되지 않습니다.")
    private String password;

    @Length(min = 8, max = 30, message = "비밀번호의 길이가 충족되지 않습니다.")
    private String newPassword;

    @Length(min = 8, max = 30, message = "비밀번호의 길이가 충족되지 않습니다.")
    private String checkPassword;
}

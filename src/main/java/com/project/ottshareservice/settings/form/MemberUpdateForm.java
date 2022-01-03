package com.project.settings.form;

import com.project.ottshareservice.domain.Member;
import lombok.Data;

@Data
public class MemberUpdateForm {

    public MemberUpdateForm(Member member) {
        this.email = member.getEmail();
        this.description = member.getDescription();
    }

    private String email;

    private String description;
}

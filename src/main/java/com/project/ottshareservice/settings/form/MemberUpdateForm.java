package com.project.ottshareservice.settings.form;

import com.project.ottshareservice.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
public class MemberUpdateForm {

    public MemberUpdateForm(Member member) {
        this.description = member.getDescription();
    }

    private String description;
}

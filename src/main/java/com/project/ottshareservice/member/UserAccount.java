package com.project.ottshareservice.member;

import com.project.ottshareservice.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {

    public Member member;

    public UserAccount(Member member) {
        super(member.getNickname(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_"+member.getRole())));
        this.member = member;
    }
}

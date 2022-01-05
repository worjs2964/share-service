package com.project.ottshareservice.home;

import com.project.ottshareservice.domain.Member;
import com.project.ottshareservice.member.MemberRepository;
import com.project.ottshareservice.member.MemberService;
import com.project.ottshareservice.member.form.SignUpForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("이재건");
        signUpForm.setEmail("test123@gmail.com");
        signUpForm.setPassword("123123123");
        memberService.signUp(signUpForm);
    }

    @DisplayName("정상적인 로그인")
    @Test
    void correct_login() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "test123@gmail.com")
                        .param("password", "123123123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());
    }

    @DisplayName("비정상적인 로그인")
    @Test
    void wrong_login() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "123test@gmail.com")
                        .param("password", "123123123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}
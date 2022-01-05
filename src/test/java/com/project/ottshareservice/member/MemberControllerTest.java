package com.project.ottshareservice.member;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("익명 사용자 회원 가입 화면 테스트")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk());
    }

    @DisplayName("잘못된 회원가입 - 비밀번호 짧음")
    @Test
    void wrong_signUp() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "이재건")
                        .param("email", "test123")
                        .param("password", "123123")
                        .param("checkPassword", "123123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("잘못된 회원가입 - 비밀번호 다름")
    @Test
    void wrong_signUp2() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "이재건")
                        .param("email", "test123")
                        .param("password", "123456789")
                        .param("checkPassword", "987654321")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("member/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("정상적인 회원가입")
    @Test
    void correct_signUp() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "이재건")
                        .param("email", "test123@gmail.com")
                        .param("password", "123123123!")
                        .param("checkPassword", "123123123!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated());

        assertTrue(memberRepository.existsByNickname("이재건"));
    }
}
package com.handwoong.everyonewaiter.controller;

import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.service.MemberService;
import com.handwoong.everyonewaiter.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (SecurityUtils.isAuthentication()) {
            return "redirect:/members/profile";
        }

        model.addAttribute("memberDto", new MemberDto.RequestDto());
        return "members/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        if (authentication != null) {
            log.info("회원 로그아웃 = 로그인 아이디 : '{}'", authentication.getName());
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (SecurityUtils.isAuthentication()) {
            return "redirect:/members/profile";
        }

        model.addAttribute("memberDto", new MemberDto.RequestDto());
        return "members/register";
    }

    @PostMapping("/register")
    public String register(@Validated MemberDto.RequestDto memberDto) {
        memberService.register(memberDto);
        return "redirect:/members/login";
    }

    @GetMapping("/account")
    public String profilePage(Model model) {
        String username = SecurityUtils.getUsername();
        MemberDto.ResponseDto memberDto = memberService.findMemberByUsername(username);
        model.addAttribute("member", memberDto);
        return "members/account";
    }
}

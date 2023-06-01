package com.handwoong.everyonewaiter.controller.member

import com.handwoong.everyonewaiter.util.isAuthentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MemberViewController {

    @GetMapping("/members/register")
    fun registerForm(): String {
        if (isAuthentication()) {
            return "redirect:/"
        }
        return "members/register"
    }

    @GetMapping("/members/login")
    fun loginForm(): String {
        if (isAuthentication()) {
            return "redirect:/"
        }
        return "members/login"
    }

}

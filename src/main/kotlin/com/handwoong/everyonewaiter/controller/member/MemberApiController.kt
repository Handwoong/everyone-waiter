package com.handwoong.everyonewaiter.controller.member

import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.member.MemberLoginRequest
import com.handwoong.everyonewaiter.dto.member.TokenResponse
import com.handwoong.everyonewaiter.service.member.MemberService
import com.handwoong.everyonewaiter.util.createCookie
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class MemberApiController(
    private val memberService: MemberService,
) {

    @PostMapping("/members/login")
    fun loginHandler(
        response: HttpServletResponse,
        @RequestBody memberDto: MemberLoginRequest,
    ): TokenResponse {
        val tokenResponse = memberService.login(memberDto)
        response.addCookie(createCookie(tokenResponse.accessToken))
        return tokenResponse
    }

    @PostMapping("/members/register")
    fun registerHandler(@RequestBody memberDto: MemberCreateRequest): ResponseEntity<Unit> {
        memberService.register(memberDto)
        return ResponseEntity<Unit>(HttpStatus.CREATED)
    }

}

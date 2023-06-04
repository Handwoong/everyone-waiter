package com.handwoong.everyonewaiter.controller.member

import com.handwoong.everyonewaiter.dto.member.MemberCreateRequest
import com.handwoong.everyonewaiter.dto.member.MemberLoginRequest
import com.handwoong.everyonewaiter.dto.member.TokenResponse
import com.handwoong.everyonewaiter.service.member.MemberService
import com.handwoong.everyonewaiter.util.createCookie
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
class MemberApiController(
    private val memberService: MemberService,
) {

    @PostMapping("/members/login")
    fun loginMember(
        response: HttpServletResponse,
        @RequestBody @Valid memberDto: MemberLoginRequest,
    ): TokenResponse {
        val tokenResponse = memberService.login(memberDto)
        response.addCookie(createCookie(tokenResponse.accessToken))
        return tokenResponse
    }

    @PostMapping("/members")
    fun registerMember(
        @RequestBody @Valid memberDto: MemberCreateRequest,
    ): ResponseEntity<Unit> {
        memberService.register(memberDto)
        return ResponseEntity<Unit>(CREATED)
    }

}

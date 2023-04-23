package com.handwoong.everyonewaiter.api;

import com.handwoong.everyonewaiter.dto.BasicResponseDto;
import com.handwoong.everyonewaiter.dto.member.MemberPasswordDto;
import com.handwoong.everyonewaiter.service.MemberService;
import com.handwoong.everyonewaiter.utils.validation.DeleteValidationGroup;
import com.handwoong.everyonewaiter.utils.validation.UpdateValidationGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PutMapping("/edit/password")
    public ResponseEntity<BasicResponseDto> changePassword(
            Authentication authentication,
            @RequestBody @Validated(UpdateValidationGroup.class) MemberPasswordDto passwordDto) {
        String username = authentication.getName();
        log.info("[PUT] 회원 비밀번호 수정 = 로그인 아이디 : '{}'", username);
        BasicResponseDto body = memberService.changePassword(username, passwordDto);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/leave")
    public ResponseEntity<BasicResponseDto> leaveMember(
            Authentication authentication,
            @RequestBody @Validated(DeleteValidationGroup.class) MemberPasswordDto passwordDto) {
        String username = authentication.getName();
        log.info("[DELETE] 회원 삭제 = 로그인 아이디 : '{}'", username);
        BasicResponseDto body = memberService.deleteMember(username, passwordDto);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}

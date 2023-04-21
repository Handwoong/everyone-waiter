package com.handwoong.everyonewaiter.dto.member;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberRegisterDtoTest {

    private static Validator validator;

    private MemberRegisterDto memberDto;


    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberRegisterDto("handwoong", "password1", "01012345678");
    }

    @Test
    @DisplayName("유효성 검사 통과")
    void validSuccess() throws Exception {
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("로그인 아이디 LowerCase")
    void lowercaseUsername() throws Exception {
        MemberRegisterDto memberDtoA = new MemberRegisterDto("HAND1", "password",
                "01012345678");

        // then
        assertThat(memberDtoA.getUsername()).isEqualTo("hand1");
    }

    @Test
    @DisplayName("로그인 아이디 공백 유효성 검사")
    void validUsernameBlank() throws Exception {
        // given
        memberDto.setUsername("");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRegisterDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "must match \"^[A-Za-z0-9]{6,20}$\"");
        }
    }

    @Test
    @DisplayName("로그인 아이디 최소 길이 유효성 검사")
    void validMinUsername() throws Exception {
        // given
        memberDto.setUsername("user");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRegisterDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "must match \"^[A-Za-z0-9]{6,20}$\"");
        }
    }

    @Test
    @DisplayName("로그인 아이디 최대 길이 유효성 검사")
    void validMaxUsername() throws Exception {
        // given
        memberDto.setUsername("abcdefghijklmnopqrstuvwxyz");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRegisterDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "must match \"^[A-Za-z0-9]{6,20}$\"");
        }
    }

    @Test
    @DisplayName("비밀번호 길이 유효성 검사")
    void validPasswordSize() throws Exception {
        // given
        memberDto.setPassword("passwd");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호 패턴 유효성 검사 (숫자 미포함)")
    void validPasswordNotNumber() throws Exception {
        // given
        memberDto.setPassword("password");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호 패턴 유효성 검사 (문자 미포함)")
    void validPasswordNotChar() throws Exception {
        // given
        memberDto.setPassword("12345678");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("휴대폰 번호 형식 유효성 검사")
    void validPhoneNumber() throws Exception {
        // given
        // Length 11
        memberDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 유효성 검사 실패")
    void validAllFail() throws Exception {
        // given
        memberDto.setUsername("user");
        memberDto.setPassword("password");
        memberDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberRegisterDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(3);
    }
}

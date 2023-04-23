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

class MemberDtoTest {

    private static Validator validator;

    private MemberDto memberDto;


    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberDto("handwoong", "password1", "01012345678");
    }

    @Test
    @DisplayName("유효성 검사 통과")
    void validSuccess() throws Exception {
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("로그인 아이디 LowerCase")
    void lowercaseUsername() throws Exception {
        MemberDto memberDtoA = new MemberDto("HAND1", "password",
                "01012345678");

        // then
        assertThat(memberDtoA.getUsername()).isEqualTo("hand1");
    }

    @Test
    @DisplayName("로그인 아이디 null 유효성 검사")
    void validUsernameNull() throws Exception {
        // given
        memberDto.setUsername(null);

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("로그인 아이디 공백 유효성 검사")
    void validUsernameBlank() throws Exception {
        // given
        memberDto.setUsername("");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.username}");
        }
    }

    @Test
    @DisplayName("로그인 아이디 최소 길이 유효성 검사")
    void validMinUsername() throws Exception {
        // given
        memberDto.setUsername("user");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.username}");
        }
    }

    @Test
    @DisplayName("로그인 아이디 최대 길이 유효성 검사")
    void validMaxUsername() throws Exception {
        // given
        memberDto.setUsername("abcdefghijklmnopqrstuvwxyz");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.username}");
        }
    }

    @Test
    @DisplayName("비밀번호 null 유효성 검사")
    void validPasswordNull() throws Exception {
        // given
        memberDto.setPassword(null);

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("비밀번호 길이 유효성 검사")
    void validPasswordSize() throws Exception {
        // given
        memberDto.setPassword("passwd");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("비밀번호 패턴 유효성 검사 (숫자 미포함)")
    void validPasswordNotNumber() throws Exception {
        // given
        memberDto.setPassword("password");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("비밀번호 패턴 유효성 검사 (문자 미포함)")
    void validPasswordNotChar() throws Exception {
        // given
        memberDto.setPassword("12345678");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("휴대폰 번호 null 유효성 검사")
    void validPhoneNumberNull() throws Exception {
        // given
        memberDto.setPhoneNumber(null);

        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("휴대폰 번호 형식 유효성 검사")
    void validPhoneNumber() throws Exception {
        // given
        // Length 11
        memberDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.phoneNumber}");
        }
    }

    @Test
    @DisplayName("모든 유효성 검사 실패")
    void validAllFail() throws Exception {
        // given
        memberDto.setUsername("user");
        memberDto.setPassword("password");
        memberDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberDto>> validResult = validator.validate(memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(3);
    }
}

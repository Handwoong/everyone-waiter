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

class MemberRequestDtoTest {

    private static Validator validator;

    private MemberRequestDto memberDto;


    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        memberDto = new MemberRequestDto("test@test.com",
                "password1", "handwoong", "01012345678");
    }

    @Test
    @DisplayName("유효성 검사 통과")
    void validSuccess() throws Exception {
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("이메일 공백 유효성 검사")
    void validEmailBlank() throws Exception {
        // given
        memberDto.setEmail("");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("must not be blank");
        }
    }

    @Test
    @DisplayName("이메일 형식 유효성 검사")
    void validEmailType() throws Exception {
        // given
        memberDto.setEmail("emailType");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "must be a well-formed email address");
        }
    }

    @Test
    @DisplayName("비밀번호 길이 유효성 검사")
    void validPasswordSize() throws Exception {
        // given
        memberDto.setPassword("passwd");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
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
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
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
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("이름 최소 길이 유효성 검사")
    void validNameLowSize() throws Exception {
        // given
        memberDto.setName("");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "size must be between 2 and 20");
        }
    }

    @Test
    @DisplayName("이름 최대 길이 유효성 검사")
    void validNameOverSize() throws Exception {
        // given
        memberDto.setName("abcdefghijklmnopqrstuvwxyz");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "size must be between 2 and 20");
        }
    }

    @Test
    @DisplayName("휴대폰 번호 형식 유효성 검사")
    void validPhoneNumber() throws Exception {
        // given
        // Length 11
        memberDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("모든 유효성 검사 실패")
    void validAllFail() throws Exception {
        // given
        memberDto.setEmail("email");
        memberDto.setPassword("password");
        memberDto.setName("");
        memberDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(4);
        for (ConstraintViolation<MemberRequestDto> result : validResult) {
            System.out.println("result.getMessage() = " + result.getMessage());
        }
    }
}

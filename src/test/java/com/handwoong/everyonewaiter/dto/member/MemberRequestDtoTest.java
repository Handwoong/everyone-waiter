package com.handwoong.everyonewaiter.dto.member;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    void validSuccess() throws Exception {
        Set<ConstraintViolation<MemberRequestDto>> validResult = validator.validate(
                memberDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
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

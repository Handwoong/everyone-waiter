package com.handwoong.everyonewaiter.dto;

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

class WaitingDtoTest {

    private static Validator validator;

    private WaitingDto.RequestDto waitingDto;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        waitingDto = new WaitingDto.RequestDto(20, 20, "01012345678");
    }

    @Test
    @DisplayName("유효성 검사 통과")
    void validSuccess() throws Exception {
        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("어른 최소 인원 실패")
    void validAdultMin() throws Exception {
        // given
        waitingDto.setAdult(-1);

        // when
        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<WaitingDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.min}");
        }
    }

    @Test
    @DisplayName("어린이 최소 인원 실패")
    void validChildrenMin() throws Exception {
        // given
        waitingDto.setChildren(-1);

        // when
        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<WaitingDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.min}");
        }
    }

    @Test
    @DisplayName("어른 최대 인원 실패")
    void validAdultMax() throws Exception {
        // given
        waitingDto.setAdult(21);

        // when
        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<WaitingDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.max}");
        }
    }

    @Test
    @DisplayName("어린이 최대 인원 실패")
    void validChildrenMax() throws Exception {
        // given
        waitingDto.setChildren(21);

        // when
        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<WaitingDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.max}");
        }
    }

    @Test
    @DisplayName("휴대폰 번호 null 유효성 검사")
    void validPhoneNumberNull() throws Exception {
        // given
        waitingDto.setPhoneNumber(null);

        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<WaitingDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("휴대폰 번호 형식 유효성 검사")
    void validPhoneNumber() throws Exception {
        // given
        // Length 11
        waitingDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<WaitingDto.RequestDto>> validResult = validator.validate(
                waitingDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<WaitingDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.phoneNumber}");
        }
    }
}
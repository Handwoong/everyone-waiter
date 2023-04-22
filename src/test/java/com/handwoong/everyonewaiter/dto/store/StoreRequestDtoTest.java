package com.handwoong.everyonewaiter.dto.store;

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

class StoreRequestDtoTest {

    private static Validator validator;

    private StoreRequestDto storeRequestDto;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        storeRequestDto = new StoreRequestDto("나루", "055-123-4567");
    }

    @Test
    @DisplayName("매장 유효성 검사 통과")
    void createStore() throws Exception {
        Set<ConstraintViolation<StoreRequestDto>> validResult = validator.validate(
                storeRequestDto);
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("매장 이름 NULL 유효성 검사")
    void validNameNotNull() throws Exception {
        // given
        storeRequestDto.setName(null);

        // when
        Set<ConstraintViolation<StoreRequestDto>> validResult = validator.validate(
                storeRequestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "must not be null");
        }
    }

    @Test
    @DisplayName("매장 이름 최소 길이 유효성 검사")
    void validNameLowSize() throws Exception {
        // given
        storeRequestDto.setName("");

        // when
        Set<ConstraintViolation<StoreRequestDto>> validResult = validator.validate(
                storeRequestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "size must be between 2 and 50");
        }
    }

    @Test
    @DisplayName("매장 이름 최대 길이 유효성 검사")
    void validNameMaxSize() throws Exception {
        // given
        storeRequestDto.setName(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        // when
        Set<ConstraintViolation<StoreRequestDto>> validResult = validator.validate(
                storeRequestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo(
                    "size must be between 2 and 50");
        }
    }

}

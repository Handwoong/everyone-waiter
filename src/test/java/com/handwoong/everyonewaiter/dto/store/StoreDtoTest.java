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

class StoreDtoTest {

    private static Validator validator;

    private StoreDto storeDto;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        storeDto = new StoreDto("나루", "055-123-4567");
    }

    @Test
    @DisplayName("매장 유효성 검사 통과")
    void createStore() throws Exception {
        Set<ConstraintViolation<StoreDto>> validResult = validator.validate(storeDto);
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("매장 이름 NULL 유효성 검사")
    void validNameNull() throws Exception {
        // given
        storeDto.setName(null);

        // when
        Set<ConstraintViolation<StoreDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("매장 이름 최소 길이 유효성 검사")
    void validNameLowSize() throws Exception {
        // given
        storeDto.setName("");

        // when
        Set<ConstraintViolation<StoreDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.size}");
        }
    }

    @Test
    @DisplayName("매장 이름 최대 길이 유효성 검사")
    void validNameMaxSize() throws Exception {
        // given
        storeDto.setName(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        // when
        Set<ConstraintViolation<StoreDto>> validResult = validator.validate(
                storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.size}");
        }
    }

    @Test
    @DisplayName("매장 전화번호 NULL 유효성 검사")
    void validTelNull() throws Exception {
        // given
        storeDto.setTelephoneNumber(null);

        // when
        Set<ConstraintViolation<StoreDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("매장 전화번호 형식 유효성 검사 실패")
    void validTelNumberFail() throws Exception {
        // given
        storeDto.setTelephoneNumber("0551234567");

        // when
        Set<ConstraintViolation<StoreDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.telNumber}");
        }
    }
}

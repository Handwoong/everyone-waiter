package com.handwoong.everyonewaiter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoreDtoTest {

    private static Validator validator;

    private StoreDto.RequestDto storeDto;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        LocalTime openTime = LocalTime.of(11, 0);
        LocalTime closeTime = LocalTime.of(21, 0);
        LocalTime startTime = LocalTime.of(15, 0);
        LocalTime endTime = LocalTime.of(16, 30);
        storeDto = new StoreDto.RequestDto("나루", "055-123-4567", startTime, endTime, openTime,
                closeTime);
    }

    @Test
    @DisplayName("ReqDto 매장 유효성 검사 통과")
    void reqDtoCreateStore() throws Exception {
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("ReqDto 매장 이름 NULL 유효성 검사")
    void validReqDtoNameNull() throws Exception {
        // given
        storeDto.setName(null);

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("ReqDto 매장 이름 최소 길이 유효성 검사")
    void validReqDtoNameLowSize() throws Exception {
        // given
        storeDto.setName("");

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.size}");
        }
    }

    @Test
    @DisplayName("ReqDto 매장 이름 최대 길이 유효성 검사")
    void validReqDtoNameMaxSize() throws Exception {
        // given
        storeDto.setName(
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.size}");
        }
    }

    @Test
    @DisplayName("ReqDto 매장 전화번호 NULL 유효성 검사")
    void validReqDtoTelNull() throws Exception {
        // given
        storeDto.setTelephoneNumber(null);

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("ReqDto 매장 전화번호 형식 유효성 검사 실패")
    void validReqDtoTelNumberFail() throws Exception {
        // given
        storeDto.setTelephoneNumber("0551234567");

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.telNumber}");
        }
    }

    @Test
    @DisplayName("ReqDto 매장 전화번호 형식 유효성 검사 성공")
    void validReqDtoTelSuccess() throws Exception {
        // given
        storeDto.setTelephoneNumber("055-123-4567");

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("ReqDto 영업 시작 시간 시간 NULL 유효성 검사")
    void validReqDtoOpenTimeFail() throws Exception {
        // given
        storeDto.setOpenTime(null);

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("ReqDto 영업 종료 시간 시간 NULL 유효성 검사")
    void validReqDtoCloseTimeFail() throws Exception {
        // given
        storeDto.setCloseTime(null);

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("ReqDto 브레이크 타임 시작 시간 시간 NULL 유효성 검사")
    void validReqDtoStartTimeFail() throws Exception {
        // given
        storeDto.setStartTime(null);

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("ReqDto 브레이크 타임 종료 시간 시간 NULL 유효성 검사")
    void validReqDtoEndTimeFail() throws Exception {
        // given
        storeDto.setEndTime(null);

        // when
        Set<ConstraintViolation<StoreDto.RequestDto>> validResult = validator.validate(storeDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<StoreDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }
}

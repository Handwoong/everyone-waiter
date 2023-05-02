package com.handwoong.everyonewaiter.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.utils.validation.DeleteValidationGroup;
import com.handwoong.everyonewaiter.utils.validation.UpdateValidationGroup;
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

    private MemberDto.RequestDto requestDto;

    private MemberDto.PwdRequestDto passwordDto;


    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        requestDto = new MemberDto.RequestDto("handwoong", "password1", "01012345678");
        passwordDto = new MemberDto.PwdRequestDto("current12345", "new12345");
    }

    @Test
    @DisplayName("RequestDto 유효성 검사 통과")
    void validReqDtoSuccess() throws Exception {
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("RequestDto 로그인 아이디 LowerCase")
    void validReqDtoLowercaseUsername() throws Exception {
        MemberDto.RequestDto memberDtoA = new MemberDto.RequestDto("HAND1", "password",
                "01012345678");

        // then
        assertThat(memberDtoA.getUsername()).isEqualTo("hand1");
    }

    @Test
    @DisplayName("RequestDto 로그인 아이디 null 유효성 검사")
    void validReqDtoUsernameNull() throws Exception {
        // given
        requestDto.setUsername(null);

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("RequestDto 로그인 아이디 공백 유효성 검사")
    void validReqDtoUsernameBlank() throws Exception {
        // given
        requestDto.setUsername("");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.username}");
        }
    }

    @Test
    @DisplayName("RequestDto 로그인 아이디 최소 길이 유효성 검사")
    void validReqDtoMinUsername() throws Exception {
        // given
        requestDto.setUsername("user");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.username}");
        }
    }

    @Test
    @DisplayName("RequestDto 로그인 아이디 최대 길이 유효성 검사")
    void validReqDtoMaxUsername() throws Exception {
        // given
        requestDto.setUsername("abcdefghijklmnopqrstuvwxyz");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.username}");
        }
    }

    @Test
    @DisplayName("RequestDto 비밀번호 null 유효성 검사")
    void validReqDtoPasswordNull() throws Exception {
        // given
        requestDto.setPassword(null);

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("RequestDto 비밀번호 길이 유효성 검사")
    void validReqDtoPasswordSize() throws Exception {
        // given
        requestDto.setPassword("passwd");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("RequestDto 비밀번호 패턴 유효성 검사 (숫자 미포함)")
    void validReqDtoPasswordNotNumber() throws Exception {
        // given
        requestDto.setPassword("password");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("RequestDto 비밀번호 패턴 유효성 검사 (문자 미포함)")
    void validReqDtoPasswordNotChar() throws Exception {
        // given
        requestDto.setPassword("12345678");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("RequestDto 휴대폰 번호 null 유효성 검사")
    void validReqDtoPhoneNumberNull() throws Exception {
        // given
        requestDto.setPhoneNumber(null);

        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.null}");
        }
    }

    @Test
    @DisplayName("RequestDto 휴대폰 번호 형식 유효성 검사")
    void validReqDtoPhoneNumber() throws Exception {
        // given
        // Length 11
        requestDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(1);
        for (ConstraintViolation<MemberDto.RequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.phoneNumber}");
        }
    }

    @Test
    @DisplayName("RequestDto 모든 유효성 검사 실패")
    void validReqDtoAllFail() throws Exception {
        // given
        requestDto.setUsername("user");
        requestDto.setPassword("password");
        requestDto.setPhoneNumber("12345678910");

        // when
        Set<ConstraintViolation<MemberDto.RequestDto>> validResult = validator.validate(requestDto);

        // then
        assertThat(validResult.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("PwdDto 비밀번호 유효성 검사 통과")
    void validPwdDtoPasswordSuccess() throws Exception {
        Set<ConstraintViolation<MemberDto.PwdRequestDto>> validResult = validator.validate(
                passwordDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("PwdDto 비밀번호 Dto 길이 유효성 검사")
    void validPwdDtoSize() throws Exception {
        // given
        passwordDto.setCurrentPassword("passwd");
        passwordDto.setNewPassword("passwd");

        // when
        Set<ConstraintViolation<MemberDto.PwdRequestDto>> validResult = validator.validate(
                passwordDto, UpdateValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(2);
        for (ConstraintViolation<MemberDto.PwdRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("PwdDto 비밀번호 패턴 유효성 검사 (숫자 미포함)")
    void validPwdDtoPasswordNotNumber() throws Exception {
        // given
        passwordDto.setCurrentPassword("password");
        passwordDto.setNewPassword("password");

        // when
        Set<ConstraintViolation<MemberDto.PwdRequestDto>> validResult = validator.validate(
                passwordDto, UpdateValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(2);
        for (ConstraintViolation<MemberDto.PwdRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("PwdDto 비밀번호 패턴 유효성 검사 (문자 미포함)")
    void validPwdDtoPasswordNotChar() throws Exception {
        // given
        passwordDto.setCurrentPassword("12345678");
        passwordDto.setNewPassword("12345678");

        // when
        Set<ConstraintViolation<MemberDto.PwdRequestDto>> validResult = validator.validate(
                passwordDto, UpdateValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(2);
        for (ConstraintViolation<MemberDto.PwdRequestDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("PwdDto Delete 그룹 검사 시 newPassword 유효성 검사는 하지 않음")
    void validPwdDtoDeleteGroup() throws Exception {
        // given
        // 유효성 검사 실패 테스트 케이스
        passwordDto.setNewPassword("12345678");

        // when
        Set<ConstraintViolation<MemberDto.PwdRequestDto>> validResult = validator.validate(
                passwordDto, DeleteValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }
}

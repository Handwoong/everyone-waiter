package com.handwoong.everyonewaiter.dto.member;

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

class MemberPasswordDtoTest {

    private static Validator validator;

    private MemberPasswordDto passwordDto;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        passwordDto = new MemberPasswordDto("current12345", "new12345");
    }

    @Test
    @DisplayName("유효성 검사 통과")
    void validSuccess() throws Exception {
        Set<ConstraintViolation<MemberPasswordDto>> validResult = validator.validate(
                passwordDto);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("비밀번호 길이 유효성 검사")
    void validPasswordSize() throws Exception {
        // given
        passwordDto.setCurrentPassword("passwd");
        passwordDto.setNewPassword("passwd");

        // when
        Set<ConstraintViolation<MemberPasswordDto>> validResult = validator.validate(
                passwordDto, UpdateValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(2);
        for (ConstraintViolation<MemberPasswordDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("비밀번호 패턴 유효성 검사 (숫자 미포함)")
    void validPasswordNotNumber() throws Exception {
        // given
        passwordDto.setCurrentPassword("password");
        passwordDto.setNewPassword("password");

        // when
        Set<ConstraintViolation<MemberPasswordDto>> validResult = validator.validate(
                passwordDto, UpdateValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(2);
        for (ConstraintViolation<MemberPasswordDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("비밀번호 패턴 유효성 검사 (문자 미포함)")
    void validPasswordNotChar() throws Exception {
        // given
        passwordDto.setCurrentPassword("12345678");
        passwordDto.setNewPassword("12345678");

        // when
        Set<ConstraintViolation<MemberPasswordDto>> validResult = validator.validate(
                passwordDto, UpdateValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(2);
        for (ConstraintViolation<MemberPasswordDto> result : validResult) {
            assertThat(result.getMessage()).isEqualTo("{error.message.password}");
        }
    }

    @Test
    @DisplayName("Delete 그룹 검사 시 newPassword 유효성 검사는 하지 않음")
    void methodName() throws Exception {
        // given
        // 유효성 검사 실패 테스트 케이스
        passwordDto.setNewPassword("12345678");

        // when
        Set<ConstraintViolation<MemberPasswordDto>> validResult = validator.validate(
                passwordDto, DeleteValidationGroup.class);

        // then
        assertThat(validResult.size()).isEqualTo(0);
    }
}

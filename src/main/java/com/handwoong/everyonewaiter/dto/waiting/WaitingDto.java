package com.handwoong.everyonewaiter.dto.waiting;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WaitingDto {

    @Min(value = 0, message = "{error.message.min}")
    @Max(value = 20, message = "{error.message.max}")
    private int adult;

    @Min(value = 0, message = "{error.message.min}")
    @Max(value = 20, message = "{error.message.max}")
    private int children;

    @NotNull(message = "{error.message.null}")
    @Pattern(regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$",
            message = "{error.message.phoneNumber}")
    private String phoneNumber;

    public WaitingDto(int adult, int children, String phoneNumber) {
        this.adult = adult;
        this.children = children;
        this.phoneNumber = phoneNumber;
    }
}

package com.handwoong.everyonewaiter.dto.waiting;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WaitingRequestDto {

    @Min(value = 0)
    @Max(value = 20)
    private int adult;

    @Min(value = 0)
    @Max(value = 20)
    private int children;

    @Pattern(regexp = "^(01[016789]{1})[0-9]{4}[0-9]{4}$")
    private String phoneNumber;

    public WaitingRequestDto(int adult, int children, String phoneNumber) {
        this.adult = adult;
        this.children = children;
        this.phoneNumber = phoneNumber;
    }
}

package com.handwoong.everyonewaiter.embedded;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class StoreBreakTime {

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "{error.message.time}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "{error.message.time}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    public StoreBreakTime(LocalTime openTime, LocalTime closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}

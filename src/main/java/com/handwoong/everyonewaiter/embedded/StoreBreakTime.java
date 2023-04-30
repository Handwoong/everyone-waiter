package com.handwoong.everyonewaiter.embedded;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class StoreBreakTime {

    @NotNull(message = "{error.message.null}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "{error.message.null}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public StoreBreakTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

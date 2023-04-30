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
public class StoreBusinessTime {

    @NotNull(message = "{error.message.null}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime openTime;

    @NotNull(message = "{error.message.null}")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    public StoreBusinessTime(LocalTime openTime, LocalTime closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}

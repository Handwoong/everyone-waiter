package com.handwoong.everyonewaiter.embedded;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class StoreBreakTime {

    private LocalTime startTime;

    private LocalTime endTime;

    public StoreBreakTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

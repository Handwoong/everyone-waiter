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
public class StoreBusinessTime {

    private LocalTime openTime;

    private LocalTime closeTime;

    public StoreBusinessTime(LocalTime openTime, LocalTime closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}

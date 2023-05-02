package com.handwoong.everyonewaiter.dto;

import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.embedded.StoreBreakTime;
import com.handwoong.everyonewaiter.embedded.StoreBusinessTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

public class StoreDto {

    @Data
    @NoArgsConstructor
    public static class RequestDto {

        @NotNull(message = "{error.message.null}")
        @Size(min = 2, max = 50, message = "{error.message.size}")
        private String name;

        @NotNull(message = "{error.message.null}")
        @Pattern(regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}$",
                message = "{error.message.telNumber}")
        private String telephoneNumber;

        @NotNull(message = "{error.message.null}")
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;

        @NotNull(message = "{error.message.null}")
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;

        @NotNull(message = "{error.message.null}")
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime openTime;

        @NotNull(message = "{error.message.null}")
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime closeTime;

        public RequestDto(String name, String telephoneNumber) {
            this.name = name;
            this.telephoneNumber = telephoneNumber;
            this.startTime = LocalTime.of(0, 0);
            this.endTime = LocalTime.of(0, 0);
            this.openTime = LocalTime.of(0, 0);
            this.closeTime = LocalTime.of(0, 0);
        }

        public RequestDto(String name, String telephoneNumber, LocalTime startTime,
                LocalTime endTime, LocalTime openTime, LocalTime closeTime) {
            this.name = name;
            this.telephoneNumber = telephoneNumber;
            this.startTime = startTime;
            this.endTime = endTime;
            this.openTime = openTime;
            this.closeTime = closeTime;
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ResponseDto {

        private Long id;

        private String name;

        private String telephoneNumber;

        private StoreBusinessTime businessTime;

        private StoreBreakTime breakTime;

        public static ResponseDto from(Store store) {
            return ResponseDto.builder()
                    .id(store.getId())
                    .name(store.getName())
                    .telephoneNumber(store.getTelephoneNumber())
                    .businessTime(store.getBusinessTime())
                    .breakTime(store.getBreakTime())
                    .build();
        }
    }
}

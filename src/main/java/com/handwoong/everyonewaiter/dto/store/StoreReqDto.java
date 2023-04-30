package com.handwoong.everyonewaiter.dto.store;

import com.handwoong.everyonewaiter.embedded.StoreBreakTime;
import com.handwoong.everyonewaiter.embedded.StoreBusinessTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreReqDto {

    @NotNull(message = "{error.message.null}")
    @Size(min = 2, max = 50, message = "{error.message.size}")
    private String name;

    @NotNull(message = "{error.message.null}")
    @Pattern(regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}$",
            message = "{error.message.telNumber}")
    private String telephoneNumber;

    @Valid
    @NotNull(message = "{error.message.null}")
    private StoreBusinessTime businessTime;

    @Valid
    @NotNull(message = "{error.message.null}")
    private StoreBreakTime breakTime;

    public StoreReqDto(String name, String telephoneNumber, StoreBusinessTime openTime,
            StoreBreakTime breakTime) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.businessTime = openTime;
        this.breakTime = breakTime;
    }
}

package com.handwoong.everyonewaiter.dto.store;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreDto {

    @NotNull(message = "{error.message.null}")
    @Size(min = 2, max = 50, message = "{error.message.size}")
    private String name;

    @NotNull(message = "{error.message.null}")
    @Pattern(regexp = "^[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}$",
            message = "{error.message.telNumber}")
    private String telephoneNumber;

    public StoreDto(String name, String telephoneNumber) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
    }
}

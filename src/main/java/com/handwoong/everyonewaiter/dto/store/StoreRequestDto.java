package com.handwoong.everyonewaiter.dto.store;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreRequestDto {

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    public StoreRequestDto(String name) {
        this.name = name;
    }
}

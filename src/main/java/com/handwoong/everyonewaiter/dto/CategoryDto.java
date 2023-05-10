package com.handwoong.everyonewaiter.dto;

import com.handwoong.everyonewaiter.domain.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CategoryDto {

    @Data
    @NoArgsConstructor
    public static class RequestDto {

        private String name;

        public RequestDto(String name) {
            this.name = name;
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ResponseDto {

        private Long id;

        private String name;

        public static CategoryDto.ResponseDto from(Category category) {
            return CategoryDto.ResponseDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }
    }
}


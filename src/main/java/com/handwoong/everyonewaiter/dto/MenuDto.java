package com.handwoong.everyonewaiter.dto;

import com.handwoong.everyonewaiter.domain.Menu;
import com.handwoong.everyonewaiter.enums.MenuStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MenuDto {

    @Data
    @NoArgsConstructor
    public static class RequestDto {

        private String name;

        private String description;

        private String notice;

        private int price;

        private MenuStatus status;

        private String image;

        private int spicy;

        private int sortingSequence;

        public RequestDto(String name, String description, String notice, int price,
                MenuStatus status, String image, int spicy, int sortingSequence) {
            this.name = name;
            this.description = description;
            this.notice = notice;
            this.price = price;
            this.status = status;
            this.image = image;
            this.spicy = spicy;
            this.sortingSequence = sortingSequence;
        }
    }

    @Data
    @Builder(access = AccessLevel.PRIVATE)
    public static class ResponseDto {

        private Long id;

        private String name;

        private String description;

        private String notice;

        private int price;

        private MenuStatus status;

        private String image;

        private int spicy;

        private int sortingSequence;

        public static MenuDto.ResponseDto from(Menu menu) {
            return ResponseDto.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .description(menu.getDescription())
                    .notice(menu.getNotice())
                    .price(menu.getPrice())
                    .status(menu.getStatus())
                    .image(menu.getImage())
                    .spicy(menu.getSpicy())
                    .sortingSequence(menu.getSortingSequence())
                    .build();
        }
    }
}

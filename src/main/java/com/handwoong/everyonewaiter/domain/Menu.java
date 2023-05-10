package com.handwoong.everyonewaiter.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.handwoong.everyonewaiter.dto.MenuDto;
import com.handwoong.everyonewaiter.enums.MenuStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    private String description;

    private String notice;

    private int price;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    private String image;

    private int spicy;

    private int sortingSequence;

    @Builder(access = PRIVATE)
    public Menu(Long id, Category category, String name, String description, String notice,
            int price, MenuStatus status, String image, int spicy, int sortingSequence) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.notice = notice;
        this.price = price;
        this.status = status;
        this.image = image;
        this.spicy = spicy;
        this.sortingSequence = sortingSequence;
    }

    public static Menu createMenu(Category category, MenuDto.RequestDto menuDto) {
        Menu menu = Menu.builder()
                .category(category)
                .name(menuDto.getName())
                .description(menuDto.getDescription())
                .notice(menuDto.getNotice())
                .price(menuDto.getPrice())
                .status(menuDto.getStatus())
                .image(menuDto.getImage())
                .spicy(menuDto.getSpicy())
                .sortingSequence(menuDto.getSortingSequence())
                .build();

        category.addMenu(menu);
        return menu;
    }

    public void update(MenuDto.RequestDto menuDto) {
        this.name = menuDto.getName();
        this.description = menuDto.getDescription();
        this.notice = menuDto.getNotice();
        this.price = menuDto.getPrice();
        this.status = menuDto.getStatus();
        this.image = menuDto.getImage();
        this.spicy = menuDto.getSpicy();
        this.sortingSequence = menuDto.getSortingSequence();
    }
}

package com.handwoong.everyonewaiter.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.handwoong.everyonewaiter.dto.CategoryDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Store store;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private final List<Menu> menuList = new ArrayList<>();

    @Builder(access = PRIVATE)
    private Category(Store store, String name) {
        this.store = store;
        this.name = name;
    }

    public static Category createCategory(Store store, CategoryDto.RequestDto categoryDto) {
        return Category.builder()
                .store(store)
                .name(categoryDto.getName())
                .build();
    }

    public void update(CategoryDto.RequestDto categoryDto) {
        this.name = categoryDto.getName();
    }

    public void addMenu(Menu menu) {
        getMenuList().add(menu);
    }
}

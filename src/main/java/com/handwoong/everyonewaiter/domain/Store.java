package com.handwoong.everyonewaiter.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.handwoong.everyonewaiter.dto.store.StoreRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    @Column(unique = true)
    private String telephoneNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(access = PRIVATE)
    private Store(String name, String telephoneNumber, Member member) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.member = member;
    }

    public static Store createStore(StoreRequestDto storeDto, Member member) {
        return Store.builder()
                .name(storeDto.getName())
                .telephoneNumber(storeDto.getTelephoneNumber())
                .member(member)
                .build();
    }
}

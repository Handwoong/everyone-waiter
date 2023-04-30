package com.handwoong.everyonewaiter.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.handwoong.everyonewaiter.dto.StoreDto;
import com.handwoong.everyonewaiter.embedded.StoreBreakTime;
import com.handwoong.everyonewaiter.embedded.StoreBusinessTime;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    private StoreBusinessTime businessTime;

    @Embedded
    private StoreBreakTime breakTime;

    private boolean waitingPossible;

    @Builder(access = PRIVATE)
    private Store(String name, String telNumber, Member member, StoreBusinessTime businessTime,
            StoreBreakTime breakTime, boolean waitingPossible) {
        this.name = name;
        this.telephoneNumber = telNumber;
        this.member = member;
        this.businessTime = businessTime;
        this.breakTime = breakTime;
        this.waitingPossible = waitingPossible;
    }

    public static Store createStore(StoreDto.RequestDto storeDto, Member member) {
        StoreBusinessTime reqBusinessTime = new StoreBusinessTime(storeDto.getOpenTime(),
                storeDto.getCloseTime());
        StoreBreakTime reqBreakTime = new StoreBreakTime(storeDto.getStartTime(),
                storeDto.getEndTime());

        return Store.builder()
                .name(storeDto.getName())
                .telNumber(storeDto.getTelephoneNumber())
                .member(member)
                .businessTime(reqBusinessTime)
                .breakTime(reqBreakTime)
                .waitingPossible(true)
                .build();
    }

    public void updateStore(StoreDto.RequestDto storeDto) {
        this.name = storeDto.getName();
        this.telephoneNumber = storeDto.getTelephoneNumber();
        this.businessTime = new StoreBusinessTime(storeDto.getOpenTime(), storeDto.getCloseTime());
        this.breakTime = new StoreBreakTime(storeDto.getStartTime(), storeDto.getEndTime());
    }
}

package com.handwoong.everyonewaiter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.StoreDto;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StoreTest {

    private StoreDto.RequestDto storeDto;

    private Member member;

    @BeforeEach
    void beforeEach() {
        MemberDto.RequestDto memberDto = new MemberDto.RequestDto("handwoong", "password",
                "01012345678");
        member = Member.createMember(memberDto);
        LocalTime openTime = LocalTime.of(11, 0);
        LocalTime closeTime = LocalTime.of(21, 0);
        LocalTime startTime = LocalTime.of(15, 0);
        LocalTime endTime = LocalTime.of(16, 30);
        storeDto = new StoreDto.RequestDto("나루", "055-123-4567", startTime, endTime, openTime,
                closeTime);
    }

    @Test
    @DisplayName("매장 엔티티 생성")
    void createStore() throws Exception {
        Store store = Store.createStore(storeDto, member);
        assertThat(store.getName()).isEqualTo("나루");
        assertThat(store.getTelephoneNumber()).isEqualTo("055-123-4567");
        assertThat(store.getMember().getUsername()).isEqualTo("handwoong");
        assertThat(store.getBreakTime().getStartTime()).isEqualTo("15:00");
        assertThat(store.getBreakTime().getEndTime()).isEqualTo("16:30");
        assertThat(store.getBusinessTime().getOpenTime()).isEqualTo("11:00");
        assertThat(store.getBusinessTime().getCloseTime()).isEqualTo("21:00");
    }

    @Test
    @DisplayName("매장 엔티티 업데이트")
    void updateStore() throws Exception {
        // given
        Store store = Store.createStore(storeDto, member);
        storeDto.setName("가배");
        storeDto.setTelephoneNumber("031-123-4567");
        storeDto.setOpenTime(LocalTime.of(9, 0));
        storeDto.setCloseTime(LocalTime.of(23, 0));
        storeDto.setStartTime(LocalTime.of(16, 0));
        storeDto.setEndTime(LocalTime.of(17, 30));

        // when
        store.updateStore(storeDto);

        // then
        assertThat(store.getName()).isEqualTo("가배");
        assertThat(store.getTelephoneNumber()).isEqualTo("031-123-4567");
        assertThat(store.getBreakTime().getStartTime()).isEqualTo("16:00");
        assertThat(store.getBreakTime().getEndTime()).isEqualTo("17:30");
        assertThat(store.getBusinessTime().getOpenTime()).isEqualTo("09:00");
        assertThat(store.getBusinessTime().getCloseTime()).isEqualTo("23:00");
    }
}

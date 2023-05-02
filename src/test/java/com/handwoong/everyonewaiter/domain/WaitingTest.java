package com.handwoong.everyonewaiter.domain;

import static com.handwoong.everyonewaiter.enums.WaitingStatus.CANCEL;
import static com.handwoong.everyonewaiter.enums.WaitingStatus.DEFAULT;
import static com.handwoong.everyonewaiter.enums.WaitingStatus.ENTER;
import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.dto.MemberDto;
import com.handwoong.everyonewaiter.dto.StoreDto;
import com.handwoong.everyonewaiter.dto.WaitingDto;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WaitingTest {

    private WaitingDto.RequestDto waitingDto;

    private Store store;

    private final List<Waiting> waitingList = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        waitingDto = new WaitingDto.RequestDto(2, 2, "01012345678");

        MemberDto.RequestDto memberDto = new MemberDto.RequestDto("handwoong", "password",
                "01011112222");
        Member member = Member.createMember(memberDto);

        LocalTime openTime = LocalTime.of(11, 0);
        LocalTime closeTime = LocalTime.of(21, 0);
        LocalTime startTime = LocalTime.of(15, 0);
        LocalTime endTime = LocalTime.of(16, 30);
        StoreDto.RequestDto storeDto = new StoreDto.RequestDto("나루", "055-123-4567", startTime,
                endTime, openTime, closeTime);
        store = Store.createStore(storeDto, member);
    }

    @Test
    @DisplayName("웨이팅 엔티티 생성")
    void createWaiting() throws Exception {
        Waiting waiting = Waiting.createWaiting(waitingDto, store, null, null);
        assertThat(waiting.getAdult()).isEqualTo(2);
        assertThat(waiting.getChildren()).isEqualTo(2);
        assertThat(waiting.getStatus()).isEqualTo(DEFAULT);
        assertThat(waiting.getWaitingNumber()).isEqualTo(1);
        assertThat(waiting.getWaitingTurn()).isEqualTo(0);
        assertThat(waiting.isSendEnterMessage()).isEqualTo(false);
        assertThat(waiting.isSendReadyMessage()).isEqualTo(false);
    }

    @Test
    @DisplayName("웨이팅 입장 상태 변경")
    void changeEnterStatus() throws Exception {
        // given
        Waiting waiting = Waiting.createWaiting(waitingDto, store, null, null);

        // when
        waiting.changeEnterOrCancelStatus(ENTER);

        // then
        assertThat(waiting.getStatus()).isEqualTo(ENTER);
    }

    @Test
    @DisplayName("웨이팅 취소 상태 변경")
    void changeCancelStatus() throws Exception {
        // given
        Waiting waiting = Waiting.createWaiting(waitingDto, store, null, null);

        // when
        waiting.changeEnterOrCancelStatus(CANCEL);

        // then
        assertThat(waiting.getStatus()).isEqualTo(CANCEL);
    }

    @Test
    @DisplayName("웨이팅 대기로 상태 변경 시 변경 되지 않음")
    void changeDefaultStatus() throws Exception {
        // given
        Waiting waiting = Waiting.createWaiting(waitingDto, store, null, null);
        waiting.changeEnterOrCancelStatus(ENTER);

        // when
        waiting.changeEnterOrCancelStatus(DEFAULT);

        // then
        assertThat(waiting.getStatus()).isEqualTo(ENTER);
    }

    @Test
    @DisplayName("입장 메시지 전송 상태 변경")
    void changeSendEnterStatus() throws Exception {
        // given
        Waiting waiting = Waiting.createWaiting(waitingDto, store, null, null);

        // when
        waiting.changeSendEnterStatus();

        // then
        assertThat(waiting.isSendEnterMessage()).isTrue();
    }

    @Test
    @DisplayName("입장 준비 메시지 전송 상태 변경")
    void changeSendReadyStatus() throws Exception {
        // given
        Waiting waiting = Waiting.createWaiting(waitingDto, store, null, null);

        // when
        waiting.changeSendReadyStatus();

        // then
        assertThat(waiting.isSendReadyMessage()).isTrue();
    }
}

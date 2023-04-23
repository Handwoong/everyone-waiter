package com.handwoong.everyonewaiter.repository;

import static com.handwoong.everyonewaiter.domain.WaitingStatus.DEFAULT;
import static com.handwoong.everyonewaiter.domain.WaitingStatus.ENTER;
import static org.assertj.core.api.Assertions.assertThat;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.domain.Waiting;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.store.StoreDto;
import com.handwoong.everyonewaiter.dto.waiting.WaitingDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class WaitingRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    private Store store;

    private Waiting waiting;

    @BeforeEach
    void beforeEach() {
        MemberDto memberDto = new MemberDto("handwoong", "password1",
                "01012345678");
        Member member = memberRepository.save(Member.createMember(memberDto));

        StoreDto storeDto = new StoreDto("나루", "055-123-4567");
        store = storeRepository.save(Store.createStore(storeDto, member));

        WaitingDto waitingDto = new WaitingDto(4, 2, "01012345678");
        waiting = waitingRepository.save(
                Waiting.createWaiting(waitingDto, store, null, null));
    }

    @Test
    @DisplayName("매장의 웨이팅 count 조회")
    void waitingCount() throws Exception {
        // when
        Long count = waitingRepository.countByStoreId(store.getId());

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("매장의 웨이팅 목록 조회")
    void findWaitingList() throws Exception {
        // when
        List<Waiting> waitingList = waitingRepository
                .findStatusWaitingList(store.getId(), DEFAULT);
        Waiting waitingA = waitingList.get(0);

        // then
        assertThat(waitingList.size()).isEqualTo(1);
        assertThat(waitingA.getWaitingNumber()).isEqualTo(1);
        assertThat(waitingA.getWaitingTurn()).isEqualTo(0);
        assertThat(waitingA.getStatus()).isEqualTo(DEFAULT);
    }

    @Test
    @DisplayName("웨이팅 상태로 마지막 웨이팅 조회")
    void findStatusLastWaiting() throws Exception {
        // given
        waiting.changeEnterOrCancelStatus(ENTER);

        // when
        List<Waiting> findWaitingList = waitingRepository
                .findStatusLastWaiting(store.getId(), ENTER, PageRequest.of(0, 1));
        Waiting findWaiting = findWaitingList.get(0);

        // then
        assertThat(findWaitingList.size()).isEqualTo(1);
        assertThat(findWaiting.getWaitingNumber()).isEqualTo(1);
        assertThat(findWaiting.getWaitingTurn()).isEqualTo(-1);
        assertThat(findWaiting.getStatus()).isEqualTo(ENTER);
    }

    @Test
    @DisplayName("매장의 마지막 웨이팅 조회")
    void findLastWaiting() throws Exception {
        // given
        WaitingDto waitingDto = new WaitingDto(4, 2, "01012345678");
        Waiting newWaiting = waitingRepository.save(
                Waiting.createWaiting(waitingDto, store, waiting, waiting));

        // when
        Waiting lastWaiting = waitingRepository.findLastWaiting(store.getId(),
                PageRequest.of(0, 1)).get(0);

        // then
        assertThat(lastWaiting.getId()).isEqualTo(newWaiting.getId());
        assertThat(lastWaiting.getWaitingNumber()).isEqualTo(2);
        assertThat(lastWaiting.getWaitingTurn()).isEqualTo(1);
        assertThat(lastWaiting.getStatus()).isEqualTo(DEFAULT);
    }

    @Test
    @DisplayName("특정 대기 순번보다 크면 대기 순번 감소")
    void greaterThanDecreaseTurn() throws Exception {
        // given
        WaitingDto waitingDto = new WaitingDto(4, 2, "01012345678");
        waitingRepository.save(
                Waiting.createWaiting(waitingDto, store, waiting, waiting));

        // when
        int changeCount = waitingRepository.decreaseWaitingTurn(store.getId(), 0);

        // then
        assertThat(changeCount).isEqualTo(1);
    }
}

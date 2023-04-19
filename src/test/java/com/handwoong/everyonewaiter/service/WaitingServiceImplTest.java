package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.handwoong.everyonewaiter.exception.ResourceExistsException;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import com.handwoong.everyonewaiter.repository.WaitingRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WaitingServiceImplTest {

    private WaitingRepository waitingRepository;

    private StoreRepository storeRepository;

    private WaitingService waitingService;

    @BeforeEach
    void beforeEach() {
        waitingRepository = Mockito.mock(WaitingRepository.class);
        storeRepository = Mockito.mock(StoreRepository.class);
        waitingService = new WaitingServiceImpl(waitingRepository, storeRepository);
    }

    @Test
    @DisplayName("존재하지 않는 매장의 웨이팅 취소 상태 변경 요청 시 예외 발생")
    void notStoreCancelWaiting() throws Exception {
        when(storeRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> waitingService.cancelWaiting(1L, UUID.randomUUID()))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage("매장이 존재하지 않습니다.");
        verify(storeRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 매장의 웨이팅 입장 상태 변경 요청 시 예외 발생")
    void notStoreEnterWaiting() throws Exception {
        when(storeRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> waitingService.enterWaiting(1L, UUID.randomUUID()))
                .isInstanceOf(ResourceExistsException.class)
                .hasMessage("매장이 존재하지 않습니다.");
        verify(storeRepository, times(1)).existsById(1L);
    }
}

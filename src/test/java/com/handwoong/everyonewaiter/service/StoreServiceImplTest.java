package com.handwoong.everyonewaiter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.handwoong.everyonewaiter.domain.Member;
import com.handwoong.everyonewaiter.domain.Store;
import com.handwoong.everyonewaiter.dto.member.MemberDto;
import com.handwoong.everyonewaiter.dto.store.StoreDto;
import com.handwoong.everyonewaiter.dto.store.StoreResponseDto;
import com.handwoong.everyonewaiter.exception.ResourceNotFoundException;
import com.handwoong.everyonewaiter.repository.MemberRepository;
import com.handwoong.everyonewaiter.repository.StoreRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    private StoreDto storeDto;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        MemberDto memberDto = new MemberDto("handwoong", "password1",
                "01012345678");
        member = Member.createMember(memberDto);
        storeDto = new StoreDto("나루", "055-123-4567");
    }

    @Test
    @DisplayName("매장 등록")
    void register() throws Exception {
        // given
        when(memberRepository.findByUsername(anyString()))
                .thenReturn(Optional.ofNullable(member));

        // when
        storeService.register(member.getUsername(), storeDto);

        // then
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("매장 등록 시 회원 로그인 아이디를 찾을 수 없음")
    void registerNotFoundUsername() throws Exception {
        assertThatThrownBy(() -> storeService.register(member.getUsername(), storeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 회원 입니다.");
        verify(storeRepository, times(0)).save(any(Store.class));
    }

    @Test
    @DisplayName("회원의 매장 목록 조회")
    void findStoreList() throws Exception {
        // given
        Store storeA = Store.createStore(storeDto, member);
        storeDto.setName("가배");
        storeDto.setTelephoneNumber("031-123-4567");
        Store storeB = Store.createStore(storeDto, member);

        when(memberRepository.findByUsername(anyString())).thenReturn(
                Optional.ofNullable(member));
        when(storeRepository.findAllByMemberId(any()))
                .thenReturn(new ArrayList<>(List.of(storeA, storeB)));

        // when
        List<StoreResponseDto> storeList = storeService
                .findStoreList(member.getUsername());
        StoreResponseDto storeDtoA = storeList.get(0);
        StoreResponseDto storeDtoB = storeList.get(1);

        // then
        assertThat(storeList.size()).isEqualTo(2);

        assertThat(storeDtoA.getName()).isEqualTo("나루");
        assertThat(storeDtoA.getId()).isEqualTo(storeA.getId());
        assertThat(storeDtoA.getTelephoneNumber()).isEqualTo(storeA.getTelephoneNumber());

        assertThat(storeDtoB.getName()).isEqualTo("가배");
        assertThat(storeDtoB.getId()).isEqualTo(storeB.getId());
        assertThat(storeDtoB.getTelephoneNumber()).isEqualTo(storeB.getTelephoneNumber());
    }

    @Test
    @DisplayName("회원의 매장 목록 조회 시 회원 로그인 아이디를 찾을 수 없음")
    void findStoreListNotFoundUsername() throws Exception {
        assertThatThrownBy(() -> storeService.findStoreList(member.getUsername()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 회원 입니다.");
        verify(storeRepository, times(0)).findAllByMemberId(anyLong());
    }
}

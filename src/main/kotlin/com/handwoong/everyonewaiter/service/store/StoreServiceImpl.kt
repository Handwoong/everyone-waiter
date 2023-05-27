package com.handwoong.everyonewaiter.service.store

import com.handwoong.everyonewaiter.domain.member.Member
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.store.StoreRequest
import com.handwoong.everyonewaiter.dto.store.StoreResponse
import com.handwoong.everyonewaiter.exception.ErrorCode.*
import com.handwoong.everyonewaiter.repository.member.MemberRepository
import com.handwoong.everyonewaiter.repository.store.StoreRepository
import com.handwoong.everyonewaiter.util.throwFail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class StoreServiceImpl(
    private val memberRepository: MemberRepository,
    private val storeRepository: StoreRepository,
) : StoreService {

    @Transactional
    override fun register(username: String, storeDto: StoreRequest) {
        val findMember = findMemberByUsername(username)
        existsTelephone(
            username = username,
            telephone = storeDto.telephone,
        )
        val createStore = Store.createStore(storeDto, findMember)
        storeRepository.save(createStore)
    }

    @Transactional
    override fun update(username: String, storeId: Long, storeDto: StoreRequest) {
        val findStore = findMemberStore(storeId, username)
        existsTelephone(
            username = username,
            telephone = storeDto.telephone,
        )
        findStore.update(storeDto)
    }

    @Transactional
    override fun delete(username: String, storeId: Long) {
        val findStore = findMemberStore(storeId, username)
        storeRepository.delete(findStore)
    }

    override fun findStore(username: String, storeId: Long): StoreResponse {
        val findStore = findMemberStore(storeId, username)
        return StoreResponse.of(findStore)
    }

    override fun findStoreList(username: String): List<StoreResponse> {
        return storeRepository.findAllStore(username)
            .map(StoreResponse::of)
    }

    private fun findMemberByUsername(username: String): Member {
        return memberRepository.findMember(username) ?: throwFail(MEMBER_NOT_FOUND)
    }

    private fun findMemberStore(storeId: Long, username: String): Store {
        return storeRepository.findStore(storeId, username) ?: throwFail(STORE_NOT_FOUND)
    }

    private fun existsTelephone(username: String, telephone: String) {
        if (storeRepository.existsStore(username, telephone)) {
            throwFail(TELEPHONE_EXISTS)
        }
    }

}

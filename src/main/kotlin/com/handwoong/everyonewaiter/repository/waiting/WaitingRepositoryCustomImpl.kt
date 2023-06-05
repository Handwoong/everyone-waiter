package com.handwoong.everyonewaiter.repository.waiting

import com.handwoong.everyonewaiter.domain.waiting.QWaiting.waiting
import com.handwoong.everyonewaiter.domain.waiting.Waiting
import com.handwoong.everyonewaiter.domain.waiting.WaitingStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.LockModeType

@Repository
class WaitingRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : WaitingRepositoryCustom {

    override fun findLockWaitingById(waitingId: UUID): Waiting? {
        return queryFactory.select(waiting)
            .from(waiting)
            .where(waiting.id.eq(waitingId))
            .setLockMode(LockModeType.PESSIMISTIC_WRITE)
            .fetchFirst()
    }

    override fun count(
        storeId: Long,
        status: WaitingStatus?,
    ): Long {
        return queryFactory.select(waiting.id.count())
            .from(waiting)
            .where(
                waiting.store.id.eq(storeId),
                status?.let { waiting.status.eq(status) },
            )
            .fetchOne() ?: 0L
    }

    override fun existsPhoneNumber(phoneNumber: String): Boolean {
        val findWaiting = queryFactory.select(waiting)
            .from(waiting)
            .where(waiting.phoneNumber.eq(phoneNumber))
            .fetch()
        return findWaiting != null
    }

    override fun findLastWaiting(
        storeId: Long,
        status: WaitingStatus?,
    ): Waiting? {
        return queryFactory.select(waiting)
            .from(waiting)
            .where(
                waiting.store.id.eq(storeId),
                status?.let { waiting.status.eq(status) },
            )
            .orderBy(waiting.createdAt.desc())
            .fetchFirst()
    }

    override fun findAllWaiting(
        storeId: Long,
        status: WaitingStatus?,
    ): List<Waiting> {
        return queryFactory.select(waiting)
            .from(waiting)
            .where(
                waiting.store.id.eq(storeId),
                status?.let { waiting.status.eq(status) },
            )
            .orderBy(waiting.createdAt.asc())
            .fetch()
    }

    override fun decreaseWaitingTurn(
        storeId: Long,
        turn: Int,
    ) {
        queryFactory.update(waiting)
            .set(waiting.turn, waiting.turn.subtract(1))
            .where(
                waiting.store.id.eq(storeId),
                waiting.turn.gt(turn),
                waiting.status.eq(WaitingStatus.WAIT),
            )
            .execute()
    }

}

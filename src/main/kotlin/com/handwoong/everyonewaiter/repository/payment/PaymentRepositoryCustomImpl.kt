package com.handwoong.everyonewaiter.repository.payment

import com.handwoong.everyonewaiter.domain.payment.Payment
import com.handwoong.everyonewaiter.domain.payment.PaymentStatus
import com.handwoong.everyonewaiter.domain.payment.QPayment.payment
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PaymentRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : PaymentRepositoryCustom {

    override fun findTablePayment(storeId: Long, tableNumber: Int): List<Payment> {
        return queryFactory.select(payment)
            .from(payment)
            .where(
                payment.store.id.eq(storeId),
                payment.tableNumber.eq(tableNumber),
                payment.status.eq(PaymentStatus.PROCEEDING)
            )
            .fetch()
    }

    override fun findStorePayment(
        storeId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<Payment> {
        return queryFactory.select(payment)
            .from(payment)
            .where(
                payment.store.id.eq(storeId),
                payment.createdAt.between(startDate, endDate.withNano(0))
            )
            .orderBy(payment.updatedAt.asc())
            .fetch()
    }

}

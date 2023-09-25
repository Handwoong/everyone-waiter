package com.handwoong.everyonewaiter.repository.receipt

import com.handwoong.everyonewaiter.domain.receipt.QReceipt.receipt
import com.handwoong.everyonewaiter.domain.receipt.Receipt
import com.handwoong.everyonewaiter.domain.receipt.ReceiptStatus
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ReceiptRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ReceiptRepositoryCustom {

    override fun findAllStorePrint(storeId: Long): List<Receipt> {
        return queryFactory.select(receipt)
            .from(receipt)
            .where(
                receipt.store.id.eq(storeId),
                receipt.status.eq(ReceiptStatus.NOT),
            )
            .fetch()
    }

}

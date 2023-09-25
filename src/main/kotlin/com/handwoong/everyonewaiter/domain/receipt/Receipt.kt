package com.handwoong.everyonewaiter.domain.receipt

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.receipt.ReceiptRequest
import javax.persistence.*

@Entity
class Receipt(

    val tableNumber: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    val store: Store,

    val memo: String,

    @OneToMany(mappedBy = "receipt", cascade = [CascadeType.ALL], orphanRemoval = true)
    val menuList: MutableList<ReceiptMenu> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var status: ReceiptStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    val id: Long? = null,
) : BaseEntity() {

    fun print() {
        status = ReceiptStatus.PRINT
    }

    companion object {
        fun createReceipt(
            store: Store,
            receiptRequest: ReceiptRequest,
        ): Receipt {
            val receipt = Receipt(
                tableNumber = receiptRequest.tableNumber,
                store = store,
                memo = receiptRequest.memo,
                status = ReceiptStatus.NOT,
            )

            receiptRequest.receiptMenuList.forEach {
                val receiptMenu = ReceiptMenu.createReceiptMenu(
                    name = it.name,
                    qty = it.qty,
                    optionName = it.optionName,
                    customOption = it.customOption,
                )
                receipt.menuList.add(receiptMenu)
                receiptMenu.addReceipt(receipt)
            }

            return receipt
        }
    }

}

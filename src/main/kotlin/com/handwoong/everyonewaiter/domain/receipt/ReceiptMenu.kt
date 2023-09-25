package com.handwoong.everyonewaiter.domain.receipt

import com.handwoong.everyonewaiter.domain.BaseEntity
import javax.persistence.*

@Entity
class ReceiptMenu(

    val name: String,

    val qty: Int,

    val optionName: String,

    val customOption: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id")
    var receipt: Receipt? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_menu_id")
    val id: Long? = null,
) : BaseEntity() {

    fun addReceipt(receipt: Receipt) {
        this.receipt = receipt
    }

    companion object {
        fun createReceiptMenu(
            name: String,
            qty: Int,
            optionName: String,
            customOption: String,
        ): ReceiptMenu {
            return ReceiptMenu(
                name = name,
                qty = qty,
                optionName = optionName,
                customOption = customOption,
            )
        }
    }

}

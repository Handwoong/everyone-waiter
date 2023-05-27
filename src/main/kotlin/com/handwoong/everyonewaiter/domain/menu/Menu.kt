package com.handwoong.everyonewaiter.domain.menu

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.category.Category
import com.handwoong.everyonewaiter.dto.menu.MenuRequest
import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
class Menu(

    var name: String,

    var description: String,

    var notice: String,

    var price: Int,

    @Enumerated(EnumType.STRING)
    var status: MenuStatus,

    var image: String,

    var spicy: Int,

    var sortingSequence: Int,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    val id: Long? = null,
) : BaseEntity() {

    fun update(menuDto: MenuRequest) {
        this.name = menuDto.name
        this.description = menuDto.description
        this.notice = menuDto.notice
        this.price = menuDto.price
        this.status = menuDto.status
        this.image = menuDto.image
        this.spicy = menuDto.spicy
        this.sortingSequence = menuDto.sortingSequence
    }

    companion object {
        fun createMenu(
            menuDto: MenuRequest,
            category: Category,
        ): Menu {
            return Menu(
                name = menuDto.name,
                description = menuDto.description,
                notice = menuDto.notice,
                price = menuDto.price,
                status = menuDto.status,
                image = menuDto.image,
                spicy = menuDto.spicy,
                sortingSequence = category.menuList.size.inc(),
                category = category,
            )
        }
    }

}

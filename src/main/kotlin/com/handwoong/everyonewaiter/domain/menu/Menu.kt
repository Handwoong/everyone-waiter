package com.handwoong.everyonewaiter.domain.menu

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.category.Category
import com.handwoong.everyonewaiter.dto.menu.MenuRequest
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class Menu(

    var name: String,

    var description: String?,

    var notice: String?,

    var price: Int,

    @Enumerated(EnumType.STRING)
    var status: MenuStatus,

    var image: String?,

    var spicy: Int,

    var sortingSequence: Int,

    @Embedded
    var option: MenuOption,

    @OneToMany(mappedBy = "menu", cascade = [CascadeType.ALL])
    var customOption: MutableList<MenuCustomOption> = mutableListOf(),

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
        this.option = MenuOption(menuDto.isSelectSpicy, menuDto.isPrintKitchen, menuDto.isSoldOut)
        this.sortingSequence = menuDto.sortingSequence
    }

    companion object {
        fun createMenu(
            menuDto: MenuRequest,
            category: Category,
        ): Menu {
            val menu = Menu(
                name = menuDto.name,
                description = menuDto.description,
                notice = menuDto.notice,
                price = menuDto.price,
                status = menuDto.status,
                image = menuDto.image,
                spicy = menuDto.spicy,
                sortingSequence = category.menuList.size + 1,
                option = MenuOption(menuDto.isSelectSpicy, menuDto.isPrintKitchen),
                category = category,
            )

            menuDto.customOption.forEach {
                menu.customOption.add(MenuCustomOption.createMenuCustomOption(it, menu))
            }
            return menu
        }
    }

}

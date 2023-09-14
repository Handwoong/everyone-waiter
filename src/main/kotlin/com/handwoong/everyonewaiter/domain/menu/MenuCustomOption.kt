package com.handwoong.everyonewaiter.domain.menu

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.dto.menu.MenuCustomOptionRequest
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
class MenuCustomOption(

    val name: String,

    val price: Int,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    val menu: Menu,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_custom_option_id")
    val id: Long? = null,
) : BaseEntity() {

    companion object {
        fun createMenuCustomOption(
            menuCustomOptionRequest: MenuCustomOptionRequest,
            menu: Menu,
        ): MenuCustomOption {
            return MenuCustomOption(
                name = menuCustomOptionRequest.name,
                price = menuCustomOptionRequest.price,
                menu = menu,
            )
        }
    }

}

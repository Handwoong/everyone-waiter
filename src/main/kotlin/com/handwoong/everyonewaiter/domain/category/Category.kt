package com.handwoong.everyonewaiter.domain.category

import com.handwoong.everyonewaiter.domain.BaseEntity
import com.handwoong.everyonewaiter.domain.menu.Menu
import com.handwoong.everyonewaiter.domain.store.Store
import com.handwoong.everyonewaiter.dto.category.CategoryRequest
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
class Category(

    var name: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    val store: Store,

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL])
    val menuList: MutableList<Menu> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id")
    val id: Long? = null,
) : BaseEntity() {

    fun update(categoryDto: CategoryRequest) {
        this.name = categoryDto.name
    }

    fun addMenu(menu: Menu) {
        this.menuList.add(menu)
    }

    companion object {
        fun createCategory(
            categoryDto: CategoryRequest,
            store: Store,
        ): Category {
            return Category(
                name = categoryDto.name,
                store = store,
            )
        }
    }

}

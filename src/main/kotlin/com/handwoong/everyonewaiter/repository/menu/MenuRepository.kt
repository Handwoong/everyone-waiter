package com.handwoong.everyonewaiter.repository.menu

import com.handwoong.everyonewaiter.domain.menu.Menu
import org.springframework.data.jpa.repository.JpaRepository

interface MenuRepository : JpaRepository<Menu, Long>, MenuRepositoryCustom

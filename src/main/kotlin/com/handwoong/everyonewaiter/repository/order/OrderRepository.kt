package com.handwoong.everyonewaiter.repository.order

import com.handwoong.everyonewaiter.domain.order.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long>, OrderRepositoryCustom

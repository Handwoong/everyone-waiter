package com.handwoong.everyonewaiter.repository.ordercall

import com.handwoong.everyonewaiter.domain.order.OrderCall
import org.springframework.data.jpa.repository.JpaRepository

interface OrderCallRepository : JpaRepository<OrderCall, Long>, OrderCallRepositoryCustom

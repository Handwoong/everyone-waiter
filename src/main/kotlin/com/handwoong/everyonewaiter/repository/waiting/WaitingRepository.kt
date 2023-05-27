package com.handwoong.everyonewaiter.repository.waiting

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WaitingRepository : JpaRepository<Waiting, UUID>, WaitingRepositoryCustom

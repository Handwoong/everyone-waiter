package com.handwoong.everyonewaiter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class EveryoneWaiterApplication

fun main(args: Array<String>) {
    runApplication<EveryoneWaiterApplication>(*args)
}

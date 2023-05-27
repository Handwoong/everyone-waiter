package com.handwoong.everyonewaiter.config.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
class QuerydslConfig(
    private val em: EntityManager,
) {

    @Bean
    fun querydsl(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }

}

package com.handwoong.everyonewaiter.util

import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootTest
class CleaningTestData {

    @Autowired
    private lateinit var repositories: List<JpaRepository<*, *>>

    @AfterEach
    fun clean() {
        repositories.forEach { it.deleteAll() }
    }

}

package com.handwoong.everyonewaiter.config.message

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sender.MessageSender

@Configuration
class MessageServiceConfig(
    @Value("\${ACCESS_KEY}") private val accessKey: String,
    @Value("\${SECRET_KEY}") private val secretKey: String,
    @Value("\${SERVICE_ID}") private val serviceId: String,
) {

    @Bean
    fun messageSender(): MessageSender {
        return MessageSender(accessKey, secretKey, serviceId)
    }

}

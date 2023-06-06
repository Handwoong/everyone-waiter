package com.handwoong.everyonewaiter.config.message.template

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import template.MessageBody
import template.alimtalk.Message

interface MessageBodyTemplate {

    fun createTemplate(waiting: Waiting): MessageBody

    fun createTemplateMessage(waiting: Waiting): MutableList<Message>

}

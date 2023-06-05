package com.handwoong.everyonewaiter.config.message.template

import com.handwoong.everyonewaiter.config.message.template.TemplateType.*
import com.handwoong.everyonewaiter.domain.waiting.Waiting
import org.springframework.stereotype.Component
import template.MessageBody

@Component
class TemplateGenerator(
    private val registerMessageTemplate: RegisterMessageTemplate,
    private val enterMessageTemplate: EnterMessageTemplate,
    private val readyMessageTemplate: ReadyMessageTemplate,
    private val cancelMessageTemplate: CancelMessageTemplate,
) {

    fun generate(templateType: TemplateType, waiting: Waiting): MessageBody {
        return when (templateType) {
            REGISTER -> registerTemplate(waiting)
            ENTER -> enterTemplate(waiting)
            READY -> readyTemplate(waiting)
            CANCEL -> cancelTemplate(waiting)
        }
    }

    private fun registerTemplate(waiting: Waiting): MessageBody {
        return registerMessageTemplate.createTemplate(waiting)
    }

    private fun enterTemplate(waiting: Waiting): MessageBody {
        return enterMessageTemplate.createTemplate(waiting)
    }

    private fun readyTemplate(waiting: Waiting): MessageBody {
        return readyMessageTemplate.createTemplate(waiting)
    }

    private fun cancelTemplate(waiting: Waiting): MessageBody {
        return cancelMessageTemplate.createTemplate(waiting)
    }

}

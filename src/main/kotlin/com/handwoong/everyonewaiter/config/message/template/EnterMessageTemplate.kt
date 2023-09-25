package com.handwoong.everyonewaiter.config.message.template

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import org.springframework.stereotype.Component
import template.MessageBody
import template.alimtalk.AlimTalkBody
import template.alimtalk.Message

@Component
class EnterMessageTemplate(
    private val buttonTemplate: ButtonTemplate,
) : MessageBodyTemplate {

    override fun createTemplate(waiting: Waiting): MessageBody {
        return AlimTalkBody(
            plusFriendId = "@narurestaurant",
            templateCode = "CustomerEnter",
            messages = createTemplateMessage(waiting),
        )
    }

    override fun createTemplateMessage(waiting: Waiting): MutableList<Message> {
        val buttons = mutableListOf(
            buttonTemplate.createWaitingCancelButton(waiting.store.id!!, waiting.id),
        )

        return mutableListOf(
            Message(
                to = waiting.phoneNumber,
                content = """
                안녕하세요.
                나루 coffee & restaurant입니다.
                
                대기번호 ${waiting.number}번 고객님 지금 입장 해 주세요.
                
                ■ 5분 이내 미 입장 시 대기 접수가 자동 취소되며, 다시 대기 등록을 해주셔야 합니다.
            """.trimIndent(),
                buttons = buttons,
            )
        )
    }

}

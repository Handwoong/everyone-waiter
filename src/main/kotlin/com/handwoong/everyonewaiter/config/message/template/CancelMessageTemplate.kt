package com.handwoong.everyonewaiter.config.message.template

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import org.springframework.stereotype.Component
import template.MessageBody
import template.alimtalk.AlimTalkBody
import template.alimtalk.Message

@Component
class CancelMessageTemplate : MessageBodyTemplate {

    override fun createTemplate(waiting: Waiting): MessageBody {
        return AlimTalkBody(
            plusFriendId = "@narurestaurant",
            templateCode = "waitingCancel",
            messages = createTemplateMessage(waiting),
        )
    }

    override fun createTemplateMessage(waiting: Waiting): MutableList<Message> {
        return mutableListOf(
            Message(
                to = waiting.phoneNumber,
                content = """
                안녕하세요.
                나루 coffee & restaurant입니다.
                
                대기번호 ${waiting.number}번 고객님 대기 취소가 완료되었습니다.
                
                오늘도 좋은 하루 보내세요.
            """.trimIndent(),
            )
        )
    }

}

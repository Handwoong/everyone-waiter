package com.handwoong.everyonewaiter.config.message.template

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import org.springframework.stereotype.Component
import template.MessageBody
import template.alimtalk.AlimTalkBody
import template.alimtalk.Message

@Component
class ReadyMessageTemplate(
    private val buttonTemplate: ButtonTemplate,
) : MessageBodyTemplate {

    override fun createTemplate(waiting: Waiting): MessageBody {
        return AlimTalkBody(
            plusFriendId = "@narurestaurant",
            templateCode = "waitingReady",
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
                
                고객님의 입장까지 2팀 남았습니다.
                매장 근처에서 대기 해 주세요.
                
                ■ 인원
                - 성인 ${waiting.adult}명
                - 아동 ${waiting.children}명
                ■ 대기번호 : ${waiting.number}번
                ■ 내 앞 대기팀 : ${waiting.turn}팀
                ■ 매장 전화번호 : ${waiting.store.telephone}
                
                찾아주신 분들께 맛있고 더 나은 서비스를 제공하기 위해 노력하겠습니다.
            """.trimIndent(),
                buttons = buttons,
            )
        )
    }

}

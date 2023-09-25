package com.handwoong.everyonewaiter.config.message.template

import com.handwoong.everyonewaiter.domain.waiting.Waiting
import org.springframework.stereotype.Component
import template.MessageBody
import template.alimtalk.AlimTalkBody
import template.alimtalk.Message

@Component
class RegisterMessageTemplate(
    private val buttonTemplate: ButtonTemplate,
) : MessageBodyTemplate {

    override fun createTemplate(waiting: Waiting): MessageBody {
        return AlimTalkBody(
            plusFriendId = "@narurestaurant",
            templateCode = "waitingRegisterFinal",
            messages = createTemplateMessage(waiting),
        )
    }

    override fun createTemplateMessage(waiting: Waiting): MutableList<Message> {
        val buttons = mutableListOf(
            buttonTemplate.createMenuInfoButton(waiting.store.id!!),
            buttonTemplate.createTurnCheckButton(waiting.store.id!!, waiting.id),
            buttonTemplate.createWaitingCancelButton(waiting.store.id!!, waiting.id),
        )

        return mutableListOf(
            Message(
                to = waiting.phoneNumber,
                content = """
                나루 coffee & restaurant을 찾아주셔서 감사합니다.
                
                고객님께서는 대기 명단에 정상적으로 접수 되셨습니다.
                
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

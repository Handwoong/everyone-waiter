package com.handwoong.everyonewaiter.config.message.template

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import template.alimtalk.Button
import template.alimtalk.ButtonType.*
import java.util.*

@Component
class ButtonTemplate(
    @Value("\${SERVER_URL}") private val serverUrl: String,
) {

    fun createMenuInfoButton(): Button {
        return Button(
            type = WL,
            name = "메뉴 미리보기",
            linkPc = "${serverUrl}/menus/stores/storeId",
            linkMobile = "${serverUrl}/menus/stores/storeId",
        )
    }

    fun createTurnCheckButton(storeId: Long, waitingId: UUID): Button {
        return Button(
            type = WL,
            name = "내 순서 확인하기",
            linkPc = "${serverUrl}/waiting/turn/${waitingId}/stores/${storeId}",
            linkMobile = "${serverUrl}/waiting/turn/${waitingId}/stores/${storeId}",
        )
    }

    fun createWaitingCancelButton(storeId: Long, waitingId: UUID): Button {
        return Button(
            type = WL,
            name = "대기 취소하기",
            linkPc = "${serverUrl}/waiting/cancel/${waitingId}/stores/${storeId}",
            linkMobile = "${serverUrl}/waiting/cancel/${waitingId}/stores/${storeId}",
        )
    }

}

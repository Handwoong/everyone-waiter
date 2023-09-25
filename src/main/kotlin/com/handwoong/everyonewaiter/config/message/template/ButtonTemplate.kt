package com.handwoong.everyonewaiter.config.message.template

import enums.ButtonType.WL
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import template.alimtalk.Button
import java.util.*

@Component
class ButtonTemplate(
    @Value("\${SERVER_URL}") private val serverUrl: String,
) {

    fun createMenuInfoButton(storeId: Long): Button {
        return Button(
            type = WL,
            name = "메뉴 미리보기",
            linkPc = "${serverUrl}/menus/stores/${storeId}",
            linkMobile = "${serverUrl}/menus/stores/${storeId}",
        )
    }

    fun createTurnCheckButton(storeId: Long, waitingId: UUID): Button {
        return Button(
            type = WL,
            name = "내 순서 확인하기",
            linkPc = "${serverUrl}/stores/${storeId}/waiting/turn/${waitingId}",
            linkMobile = "${serverUrl}/stores/${storeId}/waiting/turn/${waitingId}",
        )
    }

    fun createWaitingCancelButton(storeId: Long, waitingId: UUID): Button {
        return Button(
            type = WL,
            name = "대기 취소하기",
            linkPc = "${serverUrl}/stores/${storeId}/waiting/cancel/${waitingId}",
            linkMobile = "${serverUrl}/stores/${storeId}/waiting/cancel/${waitingId}",
        )
    }

}

package com.portfolio.fcfsreward.core.domain.reword

import com.portfolio.fcfsreward.core.domain.user.User
import java.time.LocalDate
import java.util.UUID


interface FcfsEvent {
    /*
    * Reword 이벤트 지급 id
    */
    val id: UUID

    /**
     * 이벤트 이름
     */
    val name: String

    /**
     * 설명
     */
    val description: String
}

/**
 * root
 */
data class Reword(
    override val id: UUID,
    override val name: String,
    override val description: String,
    /**
     * Kst 기준
     *
     * 2022-11-26 같은 날짜만 표현하는 key로 쓰기위해 사용
     */
    val date : LocalDate,

    /*
     * 이벤트 지급 잔여 수량
     */
    val remainCount: Long,
    /*
     * 이벤트 발급 내용
     */
    val histories: List<RewordHistory>
) : FcfsEvent{

    fun isApply(user: User) : Boolean = histories.any { it.userId == user.id }
}

interface SupplyRewordMixin {


    /**
     * 3일연속, 5일 연속, 10일 연속 보상을 받는경우
     * 300 , 400, 1000포인트를 추가로 받게됩니다.
     *
     */
    fun Collection<Reword>.getSupplyReword() {
        TODO()
    }
}

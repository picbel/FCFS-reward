package com.portfolio.fcfsreward.core.domain.reword

import com.portfolio.fcfsreward.core.domain.user.User
import java.time.LocalDate
import java.util.*


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
     * 제한 수량
     */
    val limitCount: Long,
    /*
     * 이러면 이벤트가 계쏙 지속되면
     */
    val history: List<RewordHistory>
) : FcfsEvent {
    /**
     * 3일연속, 5일 연속, 10일 연속 보상을 받는경우
     * 300 , 500, 1000포인트를 추가로 받게됩니다.
     *
     * @return 지급할 리워드 포인트
     */
    fun getSupplyReword(user: User): Long {
        history.sortedByDescending { it.date } // 역순으로 정렬
        var count = 0
        history.forEach {
            if (it.isApplied(user)) {
                ++count
            } else {
                return@forEach
            }
        }
        return when (count) {
            10 -> 1100L
            5 -> 600L
            3 -> 400L
            else -> 100L
        }
    }
}

data class RewordHistory(
    val rewordId: UUID,
    /**
     * Kst 기준
     *
     * 2022-11-26 같은 날짜만 표현하는 key로 쓰기위해 사용
     */
    val date: LocalDate,
    /*
  * 이벤트 발급 내용
  */
    val suppliedHistories: List<RewordSuppliedHistory>
) {
    /**
     * 유저가 리워드의 선착순에 든적이 있는지 검사한다.
     */
    fun isApplied(user: User): Boolean = suppliedHistories.any { it.userId == user.id }

    fun isNotApplied(user: User): Boolean = !isApplied(user)
}

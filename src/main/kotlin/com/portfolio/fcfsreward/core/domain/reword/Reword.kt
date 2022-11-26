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
    val date: LocalDate,

    /*
     * 이벤트 지급 잔여 수량
     */
    val remainCount: Long,
    /*
     * 이벤트 발급 내용
     */
    val histories: List<RewordHistory>
) : FcfsEvent {

    fun isApply(user: User): Boolean = histories.any { it.userId == user.id }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reword

        if (id != other.id) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }

}

internal interface SupplyRewordMixin {


    /**
     * 3일연속, 5일 연속, 10일 연속 보상을 받는경우
     * 300 , 500, 1000포인트를 추가로 받게됩니다.
     *
     * @return 지급할 리워드 포인트
     */
    fun Collection<Reword>.getSupplyReword(user: User): Long {
        this.sortedByDescending { it.date } // 역순으로 정렬
        var count = 0
        this.forEach {
            if (it.isApply(user)) {
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

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
     * 이러면 이벤트가 계속되서 모든 내역을 계산해야 하는 문제가 있다...
     * domain의 표현상 모든 이력을 들고있는것이 좋다 생각하지만 만약 이벤트가 1년을 넘어 2..3년이 된다면 문제가 발생할 수 있다
     * 1년지속시 예상데이터수는 365개이며
     * 안쪽의 데이터까지 생각하면 limitCount만큼 곱한 사이즈가 예상데이터 수이다
     * 현재 제한 수량 10개로 생각하니 예상되는 데이터수는 3650이다
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
        // 최근 10일 내역만 조회합니다.
        history.sortedByDescending { it.date }.take(10).run {
            var count = 0
            run breaker@{
                this.forEach {
                    if (it.isApplied(user)) {
                        if (it.isContinuousRewardReset(user)) {
                            return@breaker
                        }
                        ++count
                    } else {
                        return@breaker
                    }
                }
            }
            return when (count) {
                10, 0 -> 1100L
                5 -> 600L
                3 -> 400L
                else -> 100L
            }
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
    /**
     * 이벤트 발급 내용
     */
    val suppliedHistories: List<RewordSuppliedHistory>
) {
    /**
     * 유저가 리워드의 선착순에 든적이 있는지 검사한다.
     */
    fun isApplied(user: User): Boolean = suppliedHistories.any { it.userId == user.id }

    fun isNotApplied(user: User): Boolean = !isApplied(user)

    fun isContinuousRewardReset(user: User) = suppliedHistories.first { it.userId == user.id }.reset

}

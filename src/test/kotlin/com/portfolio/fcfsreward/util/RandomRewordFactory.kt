package com.portfolio.fcfsreward.util

import com.github.javafaker.Faker
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import java.time.LocalDate
import java.util.*

object RandomRewordFactory {
    internal fun randomReword(
        histories: List<RewordHistory> = listOf()
    ): Reword {
        return with(Faker()) {
            val id = UUID.randomUUID()
            Reword(
                id = id,
                name = this.name().fullName(),
                description = this.artist().name(),
                limitCount = 10,
                history = histories
            )
        }
    }

    internal fun randomRewordHistory(
        rewordId: UUID,
        date: LocalDate,
        userId: UUID = UUID.randomUUID(),
        userContinuous: Int = 0,
        historiesRange: IntRange = (1..9)
    ): RewordHistory {
        return RewordHistory(
            rewordId = rewordId,
            date = date,
            suppliedHistories = historiesRange.map {
                randomRewordSuppliedHistory(rewordId = rewordId, date = date, continuous = userContinuous)
            }.toMutableList().apply {
                add(
                    randomRewordSuppliedHistory(
                        rewordId = rewordId,
                        date = date,
                        userId = userId,
                        continuous = userContinuous
                    )
                )
            }.toMutableList()
        )


    }

    internal fun randomRewordSuppliedHistory(
        rewordId: UUID,
        userId: UUID = UUID.randomUUID(),
        date: LocalDate,
        continuous: Int
    ): RewordSuppliedHistory {
        return RewordSuppliedHistory(
            rewordId = rewordId,
            userId = userId,
            generateDate = date.atStartOfDay(),
            reset = (continuous % 10) == 0,
            suppliedPoint = when (continuous % 10) {
                0 -> 1100L
                5 -> 600L
                3 -> 400L
                else -> 100L
            },
            seq = null
        )

    }
}
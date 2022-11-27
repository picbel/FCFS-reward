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
        userId: UUID,
        userContinuous: Int
    ): RewordHistory {
        return RewordHistory(
            rewordId = rewordId,
            date = date,
            suppliedHistories = (1..9).map {
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
            }.toList()
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
            createDate = date.atStartOfDay(),
            reset = (continuous % 10) == 0
        )

    }
}
package com.portfolio.fcfsreward.util

import com.github.javafaker.Faker
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import java.time.LocalDate
import java.util.*

object RandomRewordFactory {
    internal fun randomReword(date: LocalDate, userId: UUID = UUID.randomUUID()): Reword {
        return with(Faker()) {
            val id = UUID.randomUUID()
            Reword(
                id = id,
                name = this.name().fullName(),
                description = this.artist().name(),
                date = date,
                remainCount = 10,
                histories = (1..9).map { randomRewordHistory(id, date) }.toMutableList().apply {
                    add(randomRewordHistory(id, date, userId))
                }
            )
        }
    }

    internal fun randomRewordHistory(rewordId: UUID, date: LocalDate, userId: UUID = UUID.randomUUID()): RewordHistory {
        return RewordHistory(
            rewordId = rewordId,
            userId = userId,
            createDate = date.atStartOfDay()
        )

    }
}
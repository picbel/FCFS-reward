package com.portfolio.fcfsreward.util

import com.github.javafaker.Faker
import com.portfolio.fcfsreward.core.domain.user.User
import java.util.*

object RandomUserFactory {
    fun randomUser(
        id: UUID = UUID.randomUUID(),
        name: String = Faker().name().fullName(),
        point: Long = 0
    ) = User(
        id = id,
        name = name,
        point = point
    )
}

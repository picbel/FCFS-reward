package com.portfolio.fcfsreward.core.domain.reword

import java.time.LocalDateTime
import java.util.*

data class RewordSuppliedHistory(
    val rewordId: UUID,
    val userId: UUID,
    val createDate: LocalDateTime
)

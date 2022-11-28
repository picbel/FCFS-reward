package com.portfolio.fcfsreward.core.domain.reword

import java.time.LocalDateTime
import java.util.*

data class RewordSuppliedHistory(
    val seq: Long?,
    val rewordId: UUID,
    val userId: UUID,
    val reset: Boolean,
    val suppliedPoint: Long,
    val generateDate: LocalDateTime
)

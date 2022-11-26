package com.portfolio.fcfsreward.core.domain.reword

import java.time.LocalDateTime
import java.util.UUID

data class RewordHistory(
    val rewordId: UUID,
    val userId: UUID,
    val createDate: LocalDateTime
)

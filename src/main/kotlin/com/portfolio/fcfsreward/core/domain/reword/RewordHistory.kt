package com.portfolio.fcfsreward.core.domain.reword

import java.time.Instant
import java.util.UUID

data class RewordHistory(
    val rewordId: UUID,
    /**
     * user id
     */
    val userId: UUID,
    val createDate: Instant
)

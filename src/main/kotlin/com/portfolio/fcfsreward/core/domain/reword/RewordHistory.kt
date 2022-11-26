package com.portfolio.fcfsreward.core.domain.reword

import java.util.UUID

data class RewordHistory(
    val userId: UUID,
    val point: Long
)

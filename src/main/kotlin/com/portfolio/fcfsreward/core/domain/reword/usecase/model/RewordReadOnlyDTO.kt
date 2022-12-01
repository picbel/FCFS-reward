package com.portfolio.fcfsreward.core.domain.reword.usecase.model

import java.time.LocalDate
import java.util.*

data class RewordReadOnlyDTO(
    val id: UUID,
    val name: String,
    val description: String,
    val limitCount: Long,
    val historyDate: List<LocalDate>
)

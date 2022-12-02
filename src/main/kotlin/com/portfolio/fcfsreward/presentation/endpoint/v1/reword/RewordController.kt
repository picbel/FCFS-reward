package com.portfolio.fcfsreward.presentation.endpoint.v1.reword

import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.usecase.RewordUseCase
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.ApplyRewordResult
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.RewordReadOnlyDTO
import com.portfolio.fcfsreward.core.domain.util.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.*


@RestController
class RewordController(
    private val useCase: RewordUseCase
) {
    @PostMapping(ApiPath.REWORD)
    fun applyReword(
        @RequestHeader("User-id")
        userId: UUID,
        @PathVariable
        rewordId: UUID
    ): ApplyRewordResult {
        return useCase.applyReword(
            rewordId = rewordId,
            userId = userId,
        )
    }

    @GetMapping(ApiPath.REWORD)
    fun getRewordDate(
        @PathVariable("rewordId")
        rewordId: UUID
    ): RewordReadOnlyDTO {
        return useCase.getRewordData(rewordId = rewordId)
    }

    @GetMapping(ApiPath.REWORD_SUPPLY_DATE)
    fun getRewordHistoryByIdAndDate(
        @PathVariable("rewordId")
        rewordId: UUID,
        @PathVariable("date")
        date: String,
        @RequestParam("sort")
        sort: Sort
    ): RewordHistory {
        return useCase.getRewordHistoryByIdAndDate(rewordId = rewordId, date = LocalDate.parse(date), sort = sort)
    }


}



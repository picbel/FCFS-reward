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
import java.util.UUID


@RestController
class RewordController(
    private val useCase: RewordUseCase
) {
    // todo 응답및 에러코드 aop 하기
    @PostMapping(ApiPath.REWORD)
    fun applyReword(
        @RequestHeader("userId")
        userId: String,
        @PathVariable
        rewordId: String
    ): ApplyRewordResult {
        return useCase.applyReword(
            rewordId = UUID.fromString(rewordId),
            userId = UUID.fromString(userId),
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
        date: LocalDate,
        @RequestParam("order")
        sort: Sort
    ): RewordHistory {
        return useCase.getRewordHistoryByIdAndDate(rewordId = rewordId, date = date, sort = sort)
    }


}



package com.portfolio.fcfsreward.core.domain.reword.repository

import com.portfolio.fcfsreward.core.common.exception.CustomException
import com.portfolio.fcfsreward.core.common.exception.ErrorCode
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.RewordReadOnlyDTO
import com.portfolio.fcfsreward.core.domain.util.Sort
import java.time.LocalDate
import java.util.*

interface RewordReadOnlyRepository {
    fun findById(rewordId: UUID): Reword?

    fun getById(rewordId: UUID): Reword = findById(rewordId) ?: throw CustomException(ErrorCode.REWORD_NOT_FOUND)

    /*
     * RewordHistory 조회후 RewordSuppliedHistory로 총 2번 조회
     */
    fun getRewordHistoryByIdAndDate(rewordId: UUID, date: LocalDate, sort: Sort): RewordHistory

    fun findRewordData(rewordId: UUID): RewordReadOnlyDTO
}

interface RewordRepository : RewordReadOnlyRepository {

    fun save(reword: Reword): Reword

    /**
     * reword에 응모하고 현재 순서를 가져옵니다
     */
    fun getApplyRewordOrder(rewordId: UUID): Long

}

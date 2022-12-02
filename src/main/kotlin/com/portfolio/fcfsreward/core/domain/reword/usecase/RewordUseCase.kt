package com.portfolio.fcfsreward.core.domain.reword.usecase

import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.ApplyRewordResult
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.RewordReadOnlyDTO
import com.portfolio.fcfsreward.core.domain.user.repository.UserRepository
import com.portfolio.fcfsreward.core.domain.util.Sort
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

interface RewordUseCase {
    fun applyReword(rewordId: UUID, userId: UUID): ApplyRewordResult

    fun getRewordData(rewordId: UUID): RewordReadOnlyDTO

    fun getRewordHistoryByIdAndDate(rewordId: UUID, date: LocalDate, sort : Sort): RewordHistory
}

@Service
internal class RewordUseCaseImpl(
    private val rewordRepo: RewordRepository,
    private val userRepo: UserRepository
) : RewordUseCase {
    override fun applyReword(rewordId: UUID, userId: UUID): ApplyRewordResult {
        val user = userRepo.findById(userId)
        val reword = rewordRepo.getById(rewordId)
        return if (reword.isNotTodayApplied(user)) {
            ApplyRewordResult(success = false, supplyPoint = 0L)
        } else {
            val order = rewordRepo.getApplyRewordOrder(rewordId)
            if (order <= reword.limitCount) {
                reword.supplyReword(user)
                rewordRepo.save(reword)
                userRepo.save(user.copy(point = reword.getSuppliedPointByUser(user, LocalDate.now())))
                ApplyRewordResult(success = true, supplyPoint = reword.getSuppliedPointByUser(user, LocalDate.now()))
            } else {
                ApplyRewordResult(success = false, supplyPoint = 0L)
            }
        }

    }

    override fun getRewordData(rewordId: UUID): RewordReadOnlyDTO {
        return rewordRepo.findRewordData(rewordId = rewordId)
    }

    override fun getRewordHistoryByIdAndDate(rewordId: UUID, date: LocalDate, sort : Sort): RewordHistory {
        return rewordRepo.getRewordHistoryByIdAndDate(rewordId = rewordId, date = date, sort = sort)
    }

}
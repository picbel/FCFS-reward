package com.portfolio.fcfsreward.core.domain.reword.usecase

import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.ApplyRewordResult
import com.portfolio.fcfsreward.core.domain.user.repository.UserRepository
import java.time.LocalDate
import java.util.*

interface RewordUseCase {
    fun applyReword(rewordId: UUID, userId: UUID): ApplyRewordResult
}


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

}
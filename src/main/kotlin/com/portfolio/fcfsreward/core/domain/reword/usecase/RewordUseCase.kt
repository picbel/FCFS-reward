package com.portfolio.fcfsreward.core.domain.reword.usecase

import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.ApplyRewordResult
import com.portfolio.fcfsreward.core.domain.user.repository.UserRepository
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
        val reword = rewordRepo.findById(rewordId)
        return if (reword.isNotTodayApplied(user)) {
            ApplyRewordResult(success = false, supplyPoint = 0L)
        } else {
            val order = rewordRepo.applyReword(rewordId)
            if (order <= reword.limitCount) {
                val supplyReword = reword.supplyReword(user)
                rewordRepo.save(supplyReword)
                userRepo.save(user)
                ApplyRewordResult(success = true, supplyPoint = 0L)
            } else {
                ApplyRewordResult(success = false, supplyPoint = 0L)
            }
        }

    }

}
package com.portfolio.fcfsreward.core.domain.reword.usecase

import java.util.*

interface RewordUseCase {
    fun applyReword(rewordId: UUID, userId: UUID) : Long
}


internal class RewordUseCaseImpl : RewordUseCase {
    override fun applyReword(rewordId: UUID, userId: UUID) : Long{
        // 리워드 조회
        // 발급 여부 검사
        // repo에 리워드 응모
        // 안들었으면 그냥 return
        // 선착순안에 들었으면 리워드 copy후 저장


        // 최근 10일 리워드 조회후 유저에 지급리워드 계산이후에 저장
        TODO("Not yet implemented")
    }

}
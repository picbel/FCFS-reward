package com.portfolio.fcfsreward.core.domain.reword.repository

import com.portfolio.fcfsreward.core.domain.reword.Reword
import java.util.*

interface RewordRepository {

    /**
     * reword에 응모하고 현재 순서를 가져옵니다
     */
    fun applyReword(rewordId: UUID) : Long

    fun findById(rewordId: UUID) : Reword

    fun findBy10DayRewords(rewordId: UUID) : List<Reword>

}

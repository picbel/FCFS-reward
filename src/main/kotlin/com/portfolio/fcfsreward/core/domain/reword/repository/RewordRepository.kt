package com.portfolio.fcfsreward.core.domain.reword.repository

import com.portfolio.fcfsreward.core.domain.reword.Reword
import java.util.*

interface RewordRepository {

    fun save(reword: Reword) : Reword

    /**
     * reword에 응모하고 현재 순서를 가져옵니다
     */
    fun getApplyRewordOrder(rewordId: UUID) : Long

    fun findById(rewordId: UUID) : Reword?
    fun getById(rewordId: UUID) : Reword = findById(rewordId) ?: throw NoSuchElementException("reword not found")

}

package com.portfolio.fcfsreward.core.domain.reword.repository

import com.portfolio.fcfsreward.core.domain.reword.Reword

interface RewordHistoryRepository {

    /**
     * reword에 응모합니다
     *
     * 지정된 갯수 이하로
     */
    fun applyReword() : Reword



}

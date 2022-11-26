package com.portfolio.fcfsreward.core.domain.reword.repository

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import java.util.UUID

interface RewordRepository {

    /**
     * reword에 응모합니다
     *
     * 응모내역을 저장합니다
     *
     * 저장 및 조회의 기능이 동시에 영속화계층에 있는 구조이지만 효율화를 위해 다음과 같이 구현하였습니다.
     */
    fun applyReword(userId: UUID) : Reword

}

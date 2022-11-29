package com.portfolio.fcfsreward.infra.domain.reword.dao

import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal interface RewordJpaDao : JpaRepository<RewordEntity, UUID>


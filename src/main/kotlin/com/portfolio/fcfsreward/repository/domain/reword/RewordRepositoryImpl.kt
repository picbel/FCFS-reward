package com.portfolio.fcfsreward.repository.domain.reword

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.repository.domain.reword.dao.RewordJpaDao
import com.portfolio.fcfsreward.repository.domain.reword.dao.RewordRedisDao
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*


@Repository
internal class RewordRepositoryImpl(
    private val redisDao: RewordRedisDao,
    private val jpaDao: RewordJpaDao
) : RewordRepository {
    override fun save(reword: Reword): Reword {
        TODO("Not yet implemented")
    }

    override fun applyReword(rewordId: UUID): Long = redisDao.applyReword(rewordId = rewordId)

    override fun findById(rewordId: UUID): Reword {
        return jpaDao.findByIdOrNull(rewordId)?.toDomain() ?: throw NoSuchElementException("reword not find")
    }

}


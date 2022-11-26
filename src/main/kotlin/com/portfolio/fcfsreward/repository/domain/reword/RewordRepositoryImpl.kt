package com.portfolio.fcfsreward.repository.domain.reword

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.repository.domain.reword.dao.RewordRedisDao
import org.springframework.stereotype.Repository
import java.util.*


@Repository
internal class RewordRepositoryImpl(
    private val redisDao: RewordRedisDao,
//    private val jpaDao: RewordJpaDao
) : RewordRepository {

    override fun applyReword(rewordId: UUID): Long = redisDao.applyReword(rewordId = rewordId)

    override fun findByIdAndToday(rewordId: UUID): Reword {
        TODO()
    }

    override fun findBy10DayRewords(rewordId: UUID): List<Reword> {
        TODO("Not yet implemented")
    }

}


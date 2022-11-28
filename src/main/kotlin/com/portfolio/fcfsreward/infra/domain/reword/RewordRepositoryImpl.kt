package com.portfolio.fcfsreward.infra.domain.reword

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.infra.domain.reword.dao.RewordJpaDao
import com.portfolio.fcfsreward.infra.domain.reword.dao.RewordRedisDao
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordEntity
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordHistoryEntity
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordHistoryId
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordSuppliedHistoryEntity
import com.portfolio.fcfsreward.infra.domain.user.dao.UserJpaDao
import com.portfolio.fcfsreward.infra.domain.user.entity.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*


@Repository
internal class RewordRepositoryImpl(
    private val redisDao: RewordRedisDao,
    private val jpaDao: RewordJpaDao,
    private val userJpaDao: UserJpaDao,
//    private val queryFactory: SpringDataQueryFactory,
) : RewordRepository {
    override fun save(reword: Reword): Reword {
        return jpaDao.save(reword.toEntity()).toDomain()
    }

    override fun getApplyRewordOrder(rewordId: UUID): Long = redisDao.getApplyRewordOrder(rewordId = rewordId)

    override fun findById(rewordId: UUID): Reword? {
        return jpaDao.findByIdOrNull(rewordId)?.toDomain()
//        return queryFactory.selectQuery<RewordEntity> {
//            select(entity(RewordEntity::class))
//            from(entity(RewordEntity::class))
//            fetch(RewordEntity::history)
//            fetch(RewordHistoryEntity::suppliedHistories)
//            where(col(RewordEntity::rewordId).equal(rewordId))
//        }.singleResult.toDomain()
    }

    private fun Reword.toEntity(): RewordEntity {
        return RewordEntity(
            rewordId = id,
            name = name,
            description = description,
            limitCount = limitCount,
        ).apply {
            history = this@toEntity.history.map { it.toEntity(this) }
        }
    }

    private fun RewordHistory.toEntity(rewordEntity: RewordEntity): RewordHistoryEntity {
        return RewordHistoryEntity(
            id = RewordHistoryId(
                rewordId = rewordId,
                date = date
            ),
            reword = rewordEntity
        ).apply {
            val users = userJpaDao.findAllById(this@toEntity.suppliedHistories.map { it.userId })
            suppliedHistories = this@toEntity.suppliedHistories.map { it.toEntity(this, users) }
        }
    }

    private fun RewordSuppliedHistory.toEntity(
        rewordHistoryEntity: RewordHistoryEntity,
        users: List<UserEntity>
    ): RewordSuppliedHistoryEntity {
        return RewordSuppliedHistoryEntity(
            seq = seq,
            rewordHistory = rewordHistoryEntity,
            user = users.first { it.id == this.userId },
            reset = reset,
            suppliedPoint = suppliedPoint,
            generateDate = generateDate
        )
    }

}


package com.portfolio.fcfsreward.infra.domain.reword

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.associate
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.selectQuery
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordReadOnlyRepository
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.infra.domain.reword.dao.RewordJpaDao
import com.portfolio.fcfsreward.infra.domain.reword.dao.RewordRedisDao
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordEntity
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordHistoryEntity
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordHistoryId
import com.portfolio.fcfsreward.infra.domain.reword.entity.RewordSuppliedHistoryEntity
import com.portfolio.fcfsreward.infra.domain.user.dao.UserJpaDao
import com.portfolio.fcfsreward.infra.domain.user.entity.UserEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*


@Repository
internal class RewordReadOnlyRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory,
    private val jpaDao: RewordJpaDao,
) : RewordReadOnlyRepository {
    override fun findById(rewordId: UUID): Reword? = jpaDao.findByIdOrNull(rewordId)?.toDomain()

    override fun getRewordHistoryByIdAndDate(rewordId: UUID, date: LocalDate): RewordHistory {
        return queryFactory.selectQuery<RewordHistoryEntity> {
            select(entity(RewordHistoryEntity::class))
            from(entity(RewordHistoryEntity::class))
            fetch(RewordHistoryEntity::suppliedHistories)
            fetch(RewordSuppliedHistoryEntity::user)
            associate(RewordHistoryEntity::id)
            where(and(
                col(RewordHistoryId::rewordId).equal(rewordId),
                col(RewordHistoryId::date).equal(date),
            ))
        }.singleResult.toDomain()
    }
}

@Repository
internal class RewordRepositoryImpl(
    @Qualifier("rewordReadOnlyRepositoryImpl")
    private val delegate: RewordReadOnlyRepository,
    private val redisDao: RewordRedisDao,
    private val jpaDao: RewordJpaDao,
    private val userJpaDao: UserJpaDao,
) : RewordRepository, RewordReadOnlyRepository by delegate {
    override fun save(reword: Reword): Reword = jpaDao.save(reword.toEntity()).toDomain()

    override fun getApplyRewordOrder(rewordId: UUID): Long = redisDao.getApplyRewordOrder(rewordId = rewordId)

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


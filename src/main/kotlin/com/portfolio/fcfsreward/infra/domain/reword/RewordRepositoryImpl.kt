package com.portfolio.fcfsreward.infra.domain.reword

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.associate
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.selectQuery
import com.portfolio.fcfsreward.core.common.exception.CustomException
import com.portfolio.fcfsreward.core.common.exception.ErrorCode
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordReadOnlyRepository
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.core.domain.reword.usecase.model.RewordReadOnlyDTO
import com.portfolio.fcfsreward.core.domain.util.Sort
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
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*
import javax.persistence.criteria.JoinType


@Repository
internal class RewordReadOnlyRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory,
    private val jpaDao: RewordJpaDao,
) : RewordReadOnlyRepository {
    override fun findById(rewordId: UUID): Reword? = queryFactory.selectQuery<RewordEntity> {
        select(entity(RewordEntity::class))
        from(entity(RewordEntity::class))
        fetch(RewordEntity::history, joinType = JoinType.LEFT)
        fetch(RewordHistoryEntity::suppliedHistories, joinType = JoinType.LEFT)
        fetch(RewordSuppliedHistoryEntity::user, joinType = JoinType.LEFT)
        where(col(RewordEntity::rewordId).equal(rewordId))
    }.singleResult.toDomain()

    override fun getRewordHistoryByIdAndDate(rewordId: UUID, date: LocalDate, sort: Sort): RewordHistory {
        return queryFactory.selectQuery<RewordHistoryEntity> {
            select(entity(RewordHistoryEntity::class))
            from(entity(RewordHistoryEntity::class))
            fetch(RewordHistoryEntity::suppliedHistories)
            fetch(RewordSuppliedHistoryEntity::user)
            associate(RewordHistoryEntity::id)
            where(
                and(
                    col(RewordHistoryId::rewordId).equal(rewordId),
                    col(RewordHistoryId::date).equal(date),
                )
            )
            when(sort) {
                Sort.DESC -> orderBy(col(RewordSuppliedHistoryEntity::generateDate).desc())
                Sort.ASC -> orderBy(col(RewordSuppliedHistoryEntity::generateDate).asc())
            }
        }.singleResult.toDomain()
    }

    override fun findRewordData(rewordId: UUID): RewordReadOnlyDTO {
        return jpaDao.findByIdOrNull(rewordId)?.let { entity ->
            RewordReadOnlyDTO(
                id = entity.rewordId,
                name = entity.name,
                description = entity.description,
                limitCount = entity.limitCount,
                historyDate = entity.history.map { it.id.date })
        } ?: throw CustomException(ErrorCode.REWORD_NOT_FOUND)
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
    @Transactional
    override fun save(reword: Reword): Reword = jpaDao.save(reword.toEntity()).toDomain()

    override fun getApplyRewordOrder(rewordId: UUID): Long = redisDao.getApplyRewordOrder(rewordId = rewordId)

    private fun Reword.toEntity(): RewordEntity {
        return RewordEntity(
            rewordId = id,
            name = name,
            title = title,
            description = description,
            limitCount = limitCount,
        ).apply {
            history = this@toEntity.history.map { it.toEntity(this) }.toSet()
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


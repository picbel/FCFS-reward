package com.portfolio.fcfsreward.repository.domain.reword.dao

import com.portfolio.fcfsreward.repository.domain.reword.entity.RewordEntity
import org.redisson.api.RedissonClient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


internal interface RewordRedisDao {
    fun applyReword(rewordId: UUID): Long
}

@Repository
internal class RewordRedisDaoImpl(
    val client: RedissonClient
) : RewordRedisDao {
    override fun applyReword(rewordId: UUID): Long {
        return client.getAtomicLong(REWORD + rewordId.toString()).incrementAndGet()
    }

    companion object {
        internal const val REWORD = "reword:"
    }
}

@Repository
internal interface RewordJpaDao : JpaRepository<RewordEntity, UUID>

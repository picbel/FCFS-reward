package com.portfolio.fcfsreward.infra.domain.reword.dao

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Repository
import java.util.*


internal interface RewordRedisDao {
    fun getApplyRewordOrder(rewordId: UUID): Long
}

@Repository
internal class RewordRedisDaoImpl(
    val client: RedissonClient
) : RewordRedisDao {
    override fun getApplyRewordOrder(rewordId: UUID): Long {
        return client.getAtomicLong(REWORD + rewordId.toString()).incrementAndGet()
    }

    companion object {
        internal const val REWORD = "reword:"
    }
}
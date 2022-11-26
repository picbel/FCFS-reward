package com.portfolio.fcfsreward.testcase.medium.core.domain.reword.repository

import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.repository.domain.reword.dao.RewordRedisDaoImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import java.util.concurrent.*


@Suppress("NonAsciiCharacters")
@SpringBootTest
//@DataRedisTest
internal class RewordRepositoryTest {

    @Autowired
    private lateinit var sut: RewordRepository

    @Autowired
    private lateinit var redisClient: RedissonClient

    private val rewordId: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        redisClient.getAtomicLong(RewordRedisDaoImpl.REWORD + rewordId.toString()).set(0)
    }

    @Test
    fun `첫번째로 리워드에 응모합니다`() {
        //given //when
        val order = sut.applyReword(rewordId)
        //then
        assertThat(order, `is`(1))
    }

    @Test
    fun `리워드에 동시에 11명의 요청이 들어옵니다`() {
        //given
        val callCount = 11
        //when
        multiApplyRewordRequestCall(callCount)
        //then
        val redisCount = redisClient.getAtomicLong(RewordRedisDaoImpl.REWORD + rewordId.toString()).get()
        assertThat(redisCount, `is`(11))
    }

    @Test
    fun `리워드에 동시에 9명의 요청이 들어옵니다 9번째 요청입니다`() {
        //given
        val callCount = 9
        //when
        multiApplyRewordRequestCall(callCount)
        //then
        val redisCount = redisClient.getAtomicLong(RewordRedisDaoImpl.REWORD + rewordId.toString()).get()
        assertThat(redisCount, `is`(9))
    }

    private fun multiApplyRewordRequestCall(callCount: Int) {
        val latch = CountDownLatch(callCount)
        val service: ExecutorService = Executors.newCachedThreadPool()
        repeat(callCount) {
            service.execute {
                sut.applyReword(rewordId)
                latch.countDown()
            }
        }
        latch.await()
    }

}



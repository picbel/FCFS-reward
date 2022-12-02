package com.portfolio.fcfsreward.testcase.medium

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@ActiveProfiles("medium")
@Configuration
class MediumRedisConfig(
    @Value("\${spring.redis.port}") redisPort: Int,
) {
    private lateinit var redisServer: RedisServer

    init {
        redisServer = RedisServer(redisPort)
    }

    @PostConstruct
    fun postConstruct() {
        redisServer.start()
    }

    @PreDestroy
    fun preDestroy() {
        redisServer.stop()
    }
}
package com.portfolio.fcfsreward.appconfig

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * 개발 테스트 편의성을 위한 test 용
 */
@Profile("dev")
@Configuration
class AlphaRedisConfig(
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
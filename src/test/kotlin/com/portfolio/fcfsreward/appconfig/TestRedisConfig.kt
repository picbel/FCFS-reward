package com.portfolio.fcfsreward.appconfig

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
class TestRedisConfig(
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
package com.portfolio.fcfsreward.appconfig

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Configuration
class RedisConfig {

    @Bean
    fun redissonClient(
        @Value("\${spring.redis.host}") redisHost: String,
        @Value("\${spring.redis.port}") redisPort: Int,
    ): RedissonClient = Redisson.create(Config().apply {
        useSingleServer().address = "redis://$redisHost:$redisPort"
        codec = StringCodec()
    })

}

@Profile("alpha")
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

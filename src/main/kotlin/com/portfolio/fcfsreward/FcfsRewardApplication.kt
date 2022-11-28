package com.portfolio.fcfsreward

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication
class FcfsRewardApplication

fun main(args: Array<String>) {
    runApplication<FcfsRewardApplication>(*args)
}

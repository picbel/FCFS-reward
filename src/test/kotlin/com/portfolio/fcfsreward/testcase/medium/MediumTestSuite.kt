package com.portfolio.fcfsreward.testcase.medium

import com.linecorp.kotlinjdsl.spring.data.autoconfigure.SpringDataQueryFactoryAutoConfiguration
import com.portfolio.fcfsreward.FcfsRewardApplication
import com.portfolio.fcfsreward.appconfig.TestRedisConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration



@DataJpaTest
@ComponentScan(
    basePackages = [
        "com.portfolio.fcfsreward.appconfig",
        "com.portfolio.fcfsreward.infra"
    ],
    basePackageClasses = [FcfsRewardApplication::class]
)
@ContextConfiguration(
    classes = [
        SpringDataQueryFactoryAutoConfiguration::class,
        TestRedisConfig::class
    ],
)
@ActiveProfiles("medium")
@EnableAutoConfiguration
abstract class MediumTestSuite
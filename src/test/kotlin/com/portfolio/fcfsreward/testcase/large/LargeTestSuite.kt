package com.portfolio.fcfsreward.testcase.large

import com.fasterxml.jackson.databind.ObjectMapper
import com.portfolio.fcfsreward.FcfsRewardApplication
import com.portfolio.fcfsreward.appconfig.TestRedisConfig
import com.portfolio.fcfsreward.appconfig.dev.RewordDateSetting
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc


@SpringBootTest(
    properties = ["spring.redis.port=9991", "spring.redis.host=localhost"],
    classes = [
        FcfsRewardApplication::class,
        TestRedisConfig::class
    ],
)
@AutoConfigureMockMvc
abstract class LargeTestSuite {

    @Autowired
    protected lateinit var mapper: ObjectMapper

    @Autowired
    protected lateinit var mockMvc: MockMvc


    companion object {
        @BeforeAll
        @JvmStatic
        fun setupAll(
            @Autowired dataSet: RewordDateSetting
        ) {
            dataSet.insertData()
        }
    }

}

//@Configuration
//class LargeRedisConfig(
//    @Value("\${spring.redis.port}") redisPort: Int,
//) {
//    private lateinit var redisServer: RedisServer
//
//    init {
//        redisServer = RedisServer(redisPort)
//    }
//
//    @PostConstruct
//    fun postConstruct() {
//        if (!redisServer.isActive){
//            redisServer.start()
//        }
//    }
//
//    @PreDestroy
//    fun preDestroy() {
//        redisServer.stop()
//    }
//}

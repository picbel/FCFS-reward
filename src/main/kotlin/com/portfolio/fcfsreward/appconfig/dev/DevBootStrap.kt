/*
 * FCFS-reward
 * Sir.LOIN Intellectual property. All rights reserved.
 */
package com.portfolio.fcfsreward.appconfig.dev


import com.portfolio.fcfsreward.appconfig.FcfsLoggerFactory.logger
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.core.domain.user.repository.UserRepository
import com.portfolio.fcfsreward.infra.domain.reword.dao.RewordRedisDaoImpl
import org.redisson.api.RedissonClient
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*



@Component
@Profile("dev")
@Order(LOWEST_PRECEDENCE)
class DevDateSetting(
    private val rewordRepo: RewordRepository,
    private val userRepo: UserRepository,
    private val redisClient: RedissonClient
) : ApplicationRunner {
    val log = logger()
    override fun run(args: ApplicationArguments?) {
        val id = UUID.fromString("a0000000-0000-0000-0000-000000000000")
        Reword(
            id = id,
            title = "매일 00시 00분 00초 선착순 10명 100 포인트 지급!!!",
            name = "reword",
            description = """
                • 보상지급방식은사용자가받기를누를때지급하게 됩니다.
                    • 선착순 10 명에게 100 포인트의 보상이 지급 되며 10 명 이후에는 지급되지 않아야 합니다.
                    • 3일 연속,5일 연속,10일 연속 보상을 받는 경우 300 포인트, 500 포인트, 1,000 포인트를 추가 보상 받게 됩니다.
                    • 추가 보상은 10일 까지 이어지며 그 이후 연속 보상 횟수는 1 회 부터 다시 시작 됩니다.
            """.trimIndent(),
            limitCount = 10,
            history = listOf(
                RewordHistory(
                    rewordId = id,
                    date = LocalDate.now(),
                    suppliedHistories = mutableListOf()
                )
            )
        ).run {
            rewordRepo.save(this)
            redisClient.getAtomicLong(RewordRedisDaoImpl.REWORD + id.toString()).set(0)
            log.info("개발 환경 셋팅입니다 오늘자 reword 이벤트를 생성합니다. id = $id")
        }
        val userIdList = mutableListOf<UUID>()
        (1..20).forEach {
            User(
                id = UUID.fromString("${idGenerate(it)}000000-0000-0000-0000-000000000000"),
                point = 0,
                name = it.toString()
            ).run {
                userIdList.add(this.id)
                userRepo.save(this)
            }
        }
        log.info("개발 환경 셋팅입니다 유저를 생성하였습니다.\n userIds = $userIdList")
    }

    private fun idGenerate(n: Int) = if (n < 10) "0$n" else n.toString()
}

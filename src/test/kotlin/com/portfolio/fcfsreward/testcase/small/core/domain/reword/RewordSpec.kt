package com.portfolio.fcfsreward.testcase.small.core.domain.reword

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.util.RandomRewordFactory.randomReword
import com.portfolio.fcfsreward.util.RandomRewordFactory.randomRewordHistory
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*


@Suppress("NonAsciiCharacters")
internal class RewordSpec {

    private lateinit var userId: UUID
    private lateinit var user: User
    private lateinit var reword: Reword

    @BeforeEach
    fun setup() {
        userId = UUID.randomUUID()
        user = User(
            id = userId,
            name = "tester",
            point = 0L
        )
        reword = randomReword()
    }

    @Test
    fun `리워드의 연속 응모횟수를 확인합니다`() {
        //given
        val sut = reword.copy(
            history = listOf(
                randomRewordHistory(
                    rewordId = reword.id,
                    date = LocalDate.now(),
                    userId = userId,
                    userContinuous = 1
                )
            )
        )
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(1))
    }

    @Test
    fun `리워드에 3번 연속 응모하였습니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..2).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(3))
    }

    @Test
    fun `리워드에 5번 연속 응모합니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..4).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(5))
    }

    @Test
    fun `리워드에 10번 연속 응모합니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..9).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10번에 리셋되어 현재 연속 응모횟수는 0번입니다
        assertThat(count, `is`(0))
    }

    @Test
    fun `리워드에 11번 연속 응모합니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..10).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(1))
    }

    @Test
    fun `리워드에 20번 연속 응모합니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..19).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10, 20에 리셋되어 0번입니다
        assertThat(count, `is`(0))
    }

    @Test
    fun `리워드에 21번 연속 응모합니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..20).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10, 20에 리셋되어 1번입니다
        assertThat(count, `is`(1))
    }

    @Test
    fun `리워드에 23번 연속 응모합니다`() {
        //given
        var userContinuous = 0
        val sut = reword.copy(history = (0..22).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++userContinuous
            )
        }.toList())
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10, 20에 리셋되어 3번입니다
        assertThat(count, `is`(3))
    }
}

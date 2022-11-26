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
    fun `리워드에 처음 응모합니다 결과 100`() {
        //given
        val sut = reword.copy(
            history = listOf(
                randomRewordHistory(
                    rewordId = reword.id,
                    date = LocalDate.now(),
                    userId = userId
                )
            )
        )
        //when
        val supplyReword = sut.getSupplyReword(user)
        //then
        assertThat(supplyReword, `is`(100))
    }

    @Test
    fun `리워드에 3번 연속 응모합니다 결과 400`() {
        //given
        val sut = reword.copy(history = (0..2).map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId
            )
        }.toList())
        //when
        val supplyReword = sut.getSupplyReword(user)
        //then
        assertThat(supplyReword, `is`(400))
    }

    @Test
    fun `리워드에 5번 연속 응모합니다 결과 600`() {
        //given
        val sut = reword.copy(history = (0..4).map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId
            )
        }.toList())
        //when
        val supplyReword = sut.getSupplyReword(user)
        //then
        assertThat(supplyReword, `is`(600))
    }

    @Test
    fun `리워드에 10번 연속 응모합니다 결과 1100`() {
        //given
        val sut = reword.copy(history = (0..9).map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId
            )
        }.toList())
        //when
        val supplyReword = sut.getSupplyReword(user)
        //then
        assertThat(supplyReword, `is`(1100))
    }

    @Test
    fun `리워드에 11번 연속 응모합니다 결과 100`() {
        //given
        val sut = reword.copy(history = (0..10).map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId
            )
        }.toList())
        //when
        val supplyReword = sut.getSupplyReword(user)
        //then
        assertThat(supplyReword, `is`(100))
    }
}

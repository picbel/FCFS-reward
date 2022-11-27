package com.portfolio.fcfsreward.testcase.small.core.domain.reword

import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.util.RandomRewordFactory.randomReword
import com.portfolio.fcfsreward.util.RandomRewordFactory.randomRewordHistory
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
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
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 3)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(3))
    }

    @Test
    fun `리워드에 5번 연속 응모합니다`() {
        //given
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 5)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(5))
    }

    @Test
    fun `리워드에 10번 연속 응모합니다`() {
        //given
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 10)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10번에 리셋되어 현재 연속 응모횟수는 0번입니다
        assertThat(count, `is`(0))
    }

    @Test
    fun `리워드에 11번 연속 응모합니다`() {
        //given
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 11)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then
        assertThat(count, `is`(1))
    }

    @Test
    fun `리워드에 20번 연속 응모합니다`() {
        //given
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 20)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10, 20에 리셋되어 0번입니다
        assertThat(count, `is`(0))
    }

    @Test
    fun `리워드에 21번 연속 응모합니다`() {
        //given
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 21)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10, 20에 리셋되어 1번입니다
        assertThat(count, `is`(1))
    }

    @Test
    fun `리워드에 23번 연속 응모합니다`() {
        //given
        val sut = `createDates 만큼 진행된 리워드를 생성합니다`(createDates = 23)
        //when
        val count = sut.getAfterResetContinuousCount(user)
        //then 10, 20에 리셋되어 3번입니다
        assertThat(count, `is`(3))
    }

    private fun `createDates 만큼 진행된 리워드를 생성합니다`(createDates: Int): Reword {
        var continuous = 0
        return reword.copy(history = (0 until createDates).reversed().map {
            randomRewordHistory(
                rewordId = reword.id,
                date = LocalDate.now().minusDays(it.toLong()),
                userId = userId,
                userContinuous = ++continuous
            )
        }.toList())
    }


    @DisplayName("리워드 포인트를 발행합니다")
    @Nested
    inner class SupplyRewordTest {
        @Test
        fun `유저가 처음으로 리워드 이벤트에 응모하엿습니다 100 포인트를 지급받습니다`() {
            //given
            val sut = reword.copy(
                history = listOf(
                    randomRewordHistory(
                        rewordId = reword.id,
                        date = LocalDate.now(),
                    )
                )
            )
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(100))
        }

        @Test
        fun `유저가 3번연속으로 리워드 이벤트에 응모하엿습니다 400 포인트를 지급받습니다`() {
            //given
            val sut = `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates = 3)
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(400))
        }

        @Test
        fun `유저가 5번연속으로 리워드 이벤트에 응모하엿습니다 600 포인트를 지급받습니다`() {
            //given
            val sut = `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates = 5)
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(600))
        }


        @Test
        fun `유저가 10번연속으로 리워드 이벤트에 응모하엿습니다 1100 포인트를 지급받습니다`() {
            //given
            val sut = `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates = 10)
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(1100))
        }

        @Test
        fun `유저가 11번연속으로 리워드 이벤트에 응모하엿습니다 100 포인트를 지급받습니다`() {
            //given
            val sut = `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates = 11)
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(100))
        }

        @Test
        fun `유저가 25번연속으로 리워드 이벤트에 응모하엿습니다 600 포인트를 지급받습니다`() {
            //given
            val sut = `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates = 25)
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(600))
        }

        @Test
        fun `유저가 26번연속으로 리워드 이벤트에 응모하엿습니다 100 포인트를 지급받습니다`() {
            //given
            val sut = `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates = 26)
            //when
            sut.supplyReword(user)
            //then
            assertThat(sut.getSuppliedPointByUser(user, LocalDate.now()), `is`(100))
        }

        private fun `오늘을 제외한 유저가 연속참여한 리워드를 생성합니다`(createDates: Int) =
            with(`createDates 만큼 진행된 리워드를 생성합니다`(createDates)) {
                this.copy(
                    history = history.toMutableList()
                        .apply {
                            removeIf { it.date == LocalDate.now() }
                            add(RewordHistory(id, LocalDate.now(), mutableListOf()))
                        }
                )
            }

    }
}

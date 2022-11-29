package com.portfolio.fcfsreward.testcase.medium.core.domain.reword.repository

import com.github.javafaker.Faker
import com.portfolio.fcfsreward.core.domain.reword.Reword
import com.portfolio.fcfsreward.core.domain.reword.RewordHistory
import com.portfolio.fcfsreward.core.domain.reword.RewordSuppliedHistory
import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.infra.domain.user.dao.UserJpaDao
import com.portfolio.fcfsreward.infra.domain.user.entity.UserEntity
import com.portfolio.fcfsreward.util.RandomUserFactory.randomUser
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.ComponentScan
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Suppress("NonAsciiCharacters")
@DataJpaTest
@ComponentScan(
    basePackages = [
        "com.portfolio.fcfsreward.appconfig",
        "com.portfolio.fcfsreward.infra"
    ]
)
class RewordRepositoryTest {

    @Autowired
    private lateinit var sut: RewordRepository

    @Autowired
    private lateinit var userJpaDao: UserJpaDao

    @PersistenceContext
    private lateinit var em: EntityManager

    private lateinit var users: MutableList<User>

    @BeforeEach
    fun setup() {
        em.clear()
        users = mutableListOf()
        repeat(2) {
            with(randomUser()) {
                users.add(this)
                userJpaDao.save(UserEntity(id, name, point))
            }

        }
    }

    @Test
    fun `reword를 저장후 조회합니다`() {
        //given
        val id = UUID.randomUUID()
        val reword = createReword(id)
        //when
        sut.save(reword)
        //then
        emFlushAndClear()
        val findReword = sut.findById(id)
        assertThat(findReword?.id, `is`(id))
    }

    @Test
    fun `reword를 저장후 수정합니다`() {
        //given
        val id = UUID.randomUUID()
        val givenUser = users.last()
        val reword = createReword(id)
        sut.save(reword)
        emFlushAndClear()
        //when
        sut.save(reword.supplyReword(givenUser))
        emFlushAndClear()
        //then reword 가 전부 영속화 되었습니다.
        val findReword = sut.findById(id)!!
        assertThat(findReword.isTodayApplied(givenUser), `is`(true))
    }

    private fun emFlushAndClear() {
        em.flush()
        em.clear()
    }

    private fun createReword(id: UUID) = with(Faker()) {
        Reword(
            id = id,
            name = this.name().fullName(),
            description = this.artist().name(),
            limitCount = 10,
            history = (0L..1L).map {
                RewordHistory(
                    rewordId = id,
                    date = LocalDate.now().minusDays(it),
                    suppliedHistories = mutableListOf(
                        RewordSuppliedHistory(
                            rewordId = id,
                            userId = users.first().id,
                            generateDate = LocalDate.now().minusDays(it).atStartOfDay(),
                            reset = false,
                            suppliedPoint = 100L,
                            seq = null
                        )
                    )
                )
            }
        )
    }

}

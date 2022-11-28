package com.portfolio.fcfsreward.testcase.medium.core.domain.reword.repository

import com.github.javafaker.Faker
import com.portfolio.fcfsreward.core.domain.reword.Reword
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
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


@Suppress("NonAsciiCharacters")
@SpringBootTest
class RewordRepositoryTest {

    @Autowired
    private lateinit var sut: RewordRepository

    @Autowired
    private lateinit var userJpaDao: UserJpaDao

    private lateinit var users: MutableList<User>

    @BeforeEach
    fun setup() {
        users = mutableListOf()
        repeat(10) {
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
        val reword = with(Faker()) {
            Reword(
                id = id,
                name = this.name().fullName(),
                description = this.artist().name(),
                limitCount = 10,
                history = listOf()
            )
        }
        //when
        sut.save(reword)
        //then
        val findReword = sut.findById(id)
        assertThat(findReword.id, `is`(id))
    }

}

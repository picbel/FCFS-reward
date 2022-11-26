package com.portfolio.fcfsreward.testcase.medium.core.domain.reword.repository

import com.portfolio.fcfsreward.core.domain.reword.repository.RewordRepository
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import java.util.concurrent.*


@Suppress("NonAsciiCharacters")
@SpringBootTest
//@DataRedisTest
internal class RewordRepositoryTest {

    @Autowired
    private lateinit var sut: RewordRepository

    fun `리워드에 응모합니다`() {
        //given

        //when

        //then
    }

    fun `리워드에 동시에 11명의 요청이 들어옵니다 11번째 요청입니다`() {
        //given

        //when

        //then
    }

    fun `리워드에 동시에 9명의 요청이 들어옵니다 9번째 요청입니다`() {
        //given

        //when

        //then
    }

    companion object {
        @BeforeAll
        @JvmStatic
        fun dataSet() {
            // 유저 10명 발급
        }
    }



}



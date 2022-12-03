package com.portfolio.fcfsreward.testcase.large

import com.portfolio.fcfsreward.core.domain.reword.usecase.model.ApplyRewordResult
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.concurrent.*


class ApplyRewordTest : LargeTestSuite() {

    @Test
    fun `15명이 동시에 리워드에 응모합니다`() {
        //given //when
        val result = requestApplyRewords(15)

        //then 선착순 10명만 성공합니다
        assertThat(result.count { it.success }, `is`(10))
    }

    private fun requestApplyRewords(repeatCount: Int) : List<ApplyRewordResult>{
        val service: ExecutorService = Executors.newFixedThreadPool(repeatCount)
        val response: MutableList<String> = mutableListOf()
        val latch = CountDownLatch(repeatCount)
        repeat(repeatCount) { index ->
            service.submit {
                val res = mockMvc.perform(
                    post("/v1/reword/a0000000-0000-0000-0000-000000000000")
                        .header(
                            "User-id",
                            UUID.fromString("${idGenerate(index)}000000-0000-0000-0000-000000000000").toString()
                        )
                ).andExpect(status().is2xxSuccessful)
                    .andReturn()
                    .response.contentAsString

                response.add(res)
                latch.countDown()
            }
        }
        latch.countDown()
        latch.await(2000, TimeUnit.SECONDS)
        return response.map { mapper.readValue(it, ApplyRewordResult::class.java) }
    }

    private fun idGenerate(n: Int) = if (n < 10) "0$n" else n.toString()
}

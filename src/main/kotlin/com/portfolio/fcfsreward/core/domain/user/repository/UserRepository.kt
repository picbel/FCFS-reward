package com.portfolio.fcfsreward.core.domain.user.repository

import com.portfolio.fcfsreward.core.common.exception.CustomException
import com.portfolio.fcfsreward.core.common.exception.ErrorCode
import com.portfolio.fcfsreward.core.domain.user.User
import java.util.*

interface UserRepository {

    fun save(user: User): User

    fun findById(id: UUID): User?

    fun getById(id: UUID): User = findById(id) ?: throw CustomException(ErrorCode.USER_NOT_FOUND)
}

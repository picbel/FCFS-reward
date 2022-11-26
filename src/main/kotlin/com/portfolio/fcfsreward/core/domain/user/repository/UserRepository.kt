package com.portfolio.fcfsreward.core.domain.user.repository

import com.portfolio.fcfsreward.core.domain.user.User
import java.util.*

interface UserRepository {

    fun save(user: User): User

    fun findById(ud: UUID): User
}

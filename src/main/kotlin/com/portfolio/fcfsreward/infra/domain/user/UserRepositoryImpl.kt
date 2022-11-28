package com.portfolio.fcfsreward.infra.domain.user

import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.core.domain.user.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
internal class UserRepositoryImpl : UserRepository{
    override fun save(user: User): User {
        TODO("Not yet implemented")
    }

    override fun findById(ud: UUID): User {
        TODO("Not yet implemented")
    }

}

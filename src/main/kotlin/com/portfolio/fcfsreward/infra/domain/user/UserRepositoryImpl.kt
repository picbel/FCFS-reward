package com.portfolio.fcfsreward.infra.domain.user

import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.core.domain.user.repository.UserRepository
import com.portfolio.fcfsreward.infra.domain.user.dao.UserJpaDao
import com.portfolio.fcfsreward.infra.domain.user.entity.UserEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
internal class UserRepositoryImpl(
    private val jpaDao: UserJpaDao
) : UserRepository {

    @Transactional
    override fun save(user: User): User {
        return jpaDao.save(UserEntity(user.id, user.name, user.point)).toDomain()
    }

    override fun findById(id: UUID): User? {
        return jpaDao.findByIdOrNull(id)?.toDomain()
    }

}

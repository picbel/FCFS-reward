package com.portfolio.fcfsreward.repository.domain.user.entity

import com.portfolio.fcfsreward.core.domain.user.User
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
internal class UserEntity (
    @Id
    val id : UUID,
    val name: String,
    val point: Long,
) {
    fun toDomain() : User = User(id, name, point)
}
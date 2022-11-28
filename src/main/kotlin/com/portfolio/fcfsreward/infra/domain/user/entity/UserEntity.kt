package com.portfolio.fcfsreward.infra.domain.user.entity

import com.portfolio.fcfsreward.core.domain.user.User
import com.portfolio.fcfsreward.infra.util.converter.UuidConverter
import java.util.*
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity
internal class UserEntity (
    @Id
    @Convert(converter = UuidConverter::class)
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    val id : UUID,
    @Column(name = "name")
    val name: String,
    @Column(name = "point")
    val point: Long,
) {
    fun toDomain() : User = User(id, name, point)
}
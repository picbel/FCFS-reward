package com.portfolio.fcfsreward.core.domain.user

import java.util.UUID


/**
* root
*/
data class User(
    val id : UUID,
    val name: String,
    val point: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

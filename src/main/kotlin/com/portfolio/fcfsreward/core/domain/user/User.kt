package com.portfolio.fcfsreward.core.domain.user

import java.util.UUID


/**
* root
*/
data class User(
    val id : UUID,
    val name: String,
    val point: Long,
)

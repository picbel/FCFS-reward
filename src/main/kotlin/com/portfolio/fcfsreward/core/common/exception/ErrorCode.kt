package com.portfolio.fcfsreward.core.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    REWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "reword not found"),

    DUPLICATE_APPLY_REWORD(HttpStatus.BAD_REQUEST, "You cannot participate in the event twice")
}

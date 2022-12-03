package com.portfolio.fcfsreward.core.common.exception


class CustomException(
    val errorCode: ErrorCode
) : RuntimeException()
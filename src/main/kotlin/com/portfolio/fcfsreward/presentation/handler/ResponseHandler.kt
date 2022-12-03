package com.portfolio.fcfsreward.presentation.handler

import com.portfolio.fcfsreward.core.common.exception.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.portfolio.fcfsreward.presentation"])
class ResponseHandler {

    @ExceptionHandler(value = [CustomException::class])
    fun handlingCustomException(ex: CustomException): ResponseEntity<ErrorResponse> = ResponseEntity(
        ErrorResponse(ex.errorCode.message),
        ex.errorCode.status
    )

    @ResponseBody
    data class ErrorResponse(
        val message: String,
    )

}

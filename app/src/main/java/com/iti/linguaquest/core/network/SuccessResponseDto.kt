package com.iti.linguaquest.core.network


data class SuccessResponseDto<T>(
    val success: Boolean,
    val data: T
)

data class ErrorResponseDto(
    val success: Boolean,
    val error: ErrorBodyDto
)

data class ErrorBodyDto(
    val apiPath: String,
    val errorCode: Int,
    val errorKey: String,
    val errorMessage: String,
    val errorTime: String
)
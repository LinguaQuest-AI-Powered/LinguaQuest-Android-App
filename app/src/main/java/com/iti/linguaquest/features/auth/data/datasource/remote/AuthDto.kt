package com.iti.linguaquest.features.auth.data.datasource.remote


data class UserDto(
    val id: Int,
    val username: String?,
    val name: String,
    val photo: String?,
    val nativeLanguage: String?,
    val isVerified: Boolean,
    val targetLanguages: List<String>
)


data class RegisterRequestDto(
    val email: String,
    val username: String,
    val password: String,
    val nativeLanguage: String,
    val targetLanguage: String
)

data class RegisterResponseDataDto(
    val id: Int,
    val email: String,
    val username: String,
    val nativeLanguage: String,
    val targetLanguage: String,
    val isVerified: Boolean
)


data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDataDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val user: UserDto
)


data class OAuthGoogleRequestDto(
    val idToken: String
)


data class OAuthResponseDataDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val isNewUser: Boolean,
    val profileComplete: Boolean,
    val user: UserDto
)

data class OtpSendRequestDto(
    val email: String,
    val purpose: String
)

data class OtpVerifyRequestDto(
    val email: String,
    val otp: String
)

data class VerifyResetOtpResponseDto(
    val resetToken: String,
    val expiresIn: Int? = null
)

data class ResetPasswordRequestDto(
    val resetToken: String,
    val newPassword: String
)
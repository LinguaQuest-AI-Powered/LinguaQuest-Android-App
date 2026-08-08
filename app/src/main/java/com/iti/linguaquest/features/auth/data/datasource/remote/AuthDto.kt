package com.iti.linguaquest.features.auth.data.datasource.remote


data class UserDto(
    val id: Int,
    val username: String?,
    val photo: String?,
    val nativeLanguage: TargetLanguageDto?,
    val isVerified: Boolean,
    val targetLanguages: List<String>
)

data class TargetLanguageDto(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String? = null
)

data class AuthLanguageOptionDto(
    val id: Int,
    val name: String,
    val code: String,
    val imageUrl: String,
    val isAdded: Boolean
)

data class AuthLanguagesResponseDataDto(
    val languages: List<AuthLanguageOptionDto>
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
    val nativeLanguage: TargetLanguageDto?,
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

data class CompleteProfileRequestDto(
    val nativeLanguageId: Int,
    val targetLanguageId: Int,
    val username: String? = null
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

data class LogoutRequestDto(
    val refreshToken: String,
    val allDevices: Boolean = false
)

data class RefreshTokenRequestDto(
    val refreshToken: String
)

data class RefreshTokenResponseDataDto(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Int
)

data class VerifyEmailResponseDto(
    val isVerified: Boolean
)

package com.iti.linguaquest.features.auth.data.mapper

import com.iti.linguaquest.features.auth.data.datasource.remote.OAuthResponseDataDto
import com.iti.linguaquest.features.auth.data.datasource.remote.UserDto
import com.iti.linguaquest.features.auth.data.datasource.remote.TargetLanguageDto
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.model.GoogleSignInResult
import com.iti.linguaquest.features.home.domain.model.LanguageOption

fun UserDto.toAuthUser(): AuthUser {
    return AuthUser(
        id = this.id,
        username = this.username,
        photo = this.photo,
        nativeLanguage = this.nativeLanguage?.toLanguageOption(),
        isVerified = this.isVerified,
        targetLanguages = this.targetLanguages
    )
}

fun TargetLanguageDto.toLanguageOption(): LanguageOption {
    return LanguageOption(
        id = this.id,
        name = this.name,
        code = this.code,
        imageUrl = this.imageUrl ?: "",
        isAdded = true
    )
}

fun OAuthResponseDataDto.toGoogleSignInResult(): GoogleSignInResult {
    return GoogleSignInResult(
        profileComplete = this.profileComplete,
        user = this.user.toAuthUser()
    )
}


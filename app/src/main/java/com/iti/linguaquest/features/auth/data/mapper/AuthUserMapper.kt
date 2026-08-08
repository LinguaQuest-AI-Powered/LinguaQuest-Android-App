package com.iti.linguaquest.features.auth.data.mapper

import com.iti.linguaquest.features.auth.data.datasource.remote.OAuthResponseDataDto
import com.iti.linguaquest.features.auth.data.datasource.remote.UserDto
import com.iti.linguaquest.features.auth.domain.model.AuthUser
import com.iti.linguaquest.features.auth.domain.model.GoogleSignInResult

fun UserDto.toAuthUser(): AuthUser {
    return AuthUser(
        id = this.id,
        username = this.username,
        photo = this.photo,
        nativeLanguage = this.nativeLanguage,
        isVerified = this.isVerified,
        targetLanguages = this.targetLanguages
    )
}

fun OAuthResponseDataDto.toGoogleSignInResult(): GoogleSignInResult {
    return GoogleSignInResult(
        profileComplete = this.profileComplete,
        user = this.user.toAuthUser()
    )
}


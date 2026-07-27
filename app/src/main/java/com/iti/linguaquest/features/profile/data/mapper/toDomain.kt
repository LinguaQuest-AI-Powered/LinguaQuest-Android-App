package com.iti.linguaquest.features.profile.data.mapper

import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PasswordStatusDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.PhotoResponseDto
import com.iti.linguaquest.features.profile.data.datasource.remote.dto.ProfileDto
import com.iti.linguaquest.features.profile.domain.model.PasswordUpdateStatus
import com.iti.linguaquest.features.profile.domain.model.ProfilePhoto
import com.iti.linguaquest.features.profile.domain.model.UserProfile


fun ProfileDto.toDomain(): UserProfile {
    return UserProfile(
        id = this.id ?: 0,
        username = this.username ?: "Unknown"
    )
}

 fun PhotoResponseDto.toDomain(): ProfilePhoto {
    return ProfilePhoto(
        photoUrl = this.photoUrl
    )
}

 fun PasswordStatusDto.toDomain(): PasswordUpdateStatus {
    return PasswordUpdateStatus(
        status = this.status
    )
}
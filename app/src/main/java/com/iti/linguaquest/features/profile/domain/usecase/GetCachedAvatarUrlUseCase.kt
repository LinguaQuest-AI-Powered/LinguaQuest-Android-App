package com.iti.linguaquest.features.profile.domain.usecase


import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedAvatarUrlUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<String?> = repository.cachedAvatarUrl
}
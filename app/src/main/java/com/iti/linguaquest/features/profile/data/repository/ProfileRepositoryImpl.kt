package com.iti.linguaquest.features.profile.data.repository


import com.iti.linguaquest.features.profile.datasource.remote.ProfileRemoteDataSource
import com.iti.linguaquest.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun getProfileSummary() =
        remoteDataSource.getProfileSummary()
}

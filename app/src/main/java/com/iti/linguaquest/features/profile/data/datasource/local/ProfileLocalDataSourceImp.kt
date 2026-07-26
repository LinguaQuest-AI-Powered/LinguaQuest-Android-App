package com.iti.linguaquest.features.profile.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.iti.linguaquest.core.database.profile.ProfileDao
import com.iti.linguaquest.core.di.SessionDataStore
import com.iti.linguaquest.core.cache.token.TokenKeys
import com.iti.linguaquest.features.profile.data.mapper.toDomain
import com.iti.linguaquest.features.profile.data.mapper.toEntity
import com.iti.linguaquest.features.profile.domain.model.ProfileSummary

class ProfileLocalDataSourceImpl @Inject constructor(
    private val profileDao: ProfileDao,
    @SessionDataStore private val dataStore: DataStore<Preferences>
) : ProfileLocalDataSource {

    override val cachedProfile: Flow<ProfileSummary?> =
        profileDao.getCachedProfile().map { it?.toDomain() }

    override val cachedAvatarUrl: Flow<String?> =
        dataStore.data.map { it[TokenKeys.AVATAR_URL] }

    override suspend fun saveProfile(profile: ProfileSummary) {
        runCatching {
            profileDao.clearProfile()
            profileDao.upsertProfile(profile.toEntity())
        }

        if (profile.photoUrl != null) {
            runCatching {
                dataStore.edit { it[TokenKeys.AVATAR_URL] = profile.photoUrl }
            }
        }
    }

    override suspend fun clearProfile() {
        runCatching { profileDao.clearProfile() }
        runCatching { dataStore.edit { it.remove(TokenKeys.AVATAR_URL) } }
    }
}

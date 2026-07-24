package com.iti.linguaquest.core.database.profile

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profile LIMIT 1")
    fun getCachedProfile(): Flow<ProfileEntity?>

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getCachedProfileOnce(): ProfileEntity?

    @Query("DELETE FROM profile")
    suspend fun clearProfile()
}

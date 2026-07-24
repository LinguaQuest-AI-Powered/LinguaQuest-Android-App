package com.iti.linguaquest.core.database.home

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHomeSummary(homeEntity: HomeEntity)

    @Query("SELECT * FROM home_summary WHERE id = 1 LIMIT 1")
    fun getHomeSummary(): Flow<HomeEntity?>

    @Query("SELECT * FROM home_summary WHERE id = 1 LIMIT 1")
    suspend fun getHomeSummaryOnce(): HomeEntity?

    @Query("DELETE FROM home_summary")
    suspend fun clearHomeSummary()
}

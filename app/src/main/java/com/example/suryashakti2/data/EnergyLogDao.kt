package com.example.suryashakti2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EnergyLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: EnergyLog)

    @Query("SELECT * FROM energy_logs ORDER BY id DESC")
    fun getAllLogs(): Flow<List<EnergyLog>>

    @Query("SELECT COALESCE(SUM(savings), 0.0) FROM energy_logs WHERE dateMillis >= :startMillis")
    fun getSavingsSince(startMillis: Long): Flow<Double>
}
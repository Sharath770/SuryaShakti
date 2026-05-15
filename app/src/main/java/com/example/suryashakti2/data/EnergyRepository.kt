package com.example.suryashakti2.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EnergyRepository(
    private val energyDao: EnergyLogDao,
    private val profileDao: UserProfileDao
) {

    // 🔹 LOGS
    fun getAllLogs(): Flow<List<EnergyLog>> = energyDao.getAllLogs()

    suspend fun saveDailyLog(
        generation: Double,
        consumption: Double,
        savings: Double
    ) {
        energyDao.insertLog(
            EnergyLog(
                dateMillis = System.currentTimeMillis(),
                generation = generation,
                consumption = consumption,
                savings = savings
            )
        )
    }

    // 🔹 PROFILE (THIS WAS MISSING ❗)
    fun getProfile(): Flow<UserProfile?> = profileDao.getProfile()

    suspend fun saveUserProfile(
        capacity: Double,
        rate: Double,
        tariff: Double
    ) {
        profileDao.insertProfile(
            UserProfile(
                id = 1,
                panelCapacity = capacity,
                gridRate = rate,
                feedInTariff = tariff
            )
        )
    }

    // 🔹 REPORT
    fun getThirtyDaySavingsReport(): Flow<Double> {
        return energyDao.getAllLogs().map { logs ->
            logs.takeLast(30).sumOf { it.savings }
        }
    }
}
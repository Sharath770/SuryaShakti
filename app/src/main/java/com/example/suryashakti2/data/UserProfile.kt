package com.example.suryashakti2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(

    // ✅ Only ONE profile in app
    @PrimaryKey
    val id: Int = 1,

    val panelCapacity: Double,
    val gridRate: Double,
    val feedInTariff: Double
)
package com.example.suryashakti2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "energy_logs")
data class EnergyLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateMillis: Long,
    val generation: Double,
    val consumption: Double,
    val savings: Double
) {
    val date: String
        get() {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return sdf.format(Date(dateMillis))
        }
}
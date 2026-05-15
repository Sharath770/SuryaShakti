package com.example.suryashakti2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [EnergyLog::class, UserProfile::class],
    version = 2,
    exportSchema = false
)
abstract class SuryaShaktiDatabase : RoomDatabase() {

    abstract fun energyLogDao(): EnergyLogDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: SuryaShaktiDatabase? = null

        fun getDatabase(context: Context): SuryaShaktiDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SuryaShaktiDatabase::class.java,
                    "surya_shakti_db"
                )
                    .fallbackToDestructiveMigration()   // ✅ CORRECT PLACE
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
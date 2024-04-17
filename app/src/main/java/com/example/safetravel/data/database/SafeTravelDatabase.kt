package com.example.safetravel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.safetravel.data.model.DeviceEntity

@Database(
    version = 1,
    entities = [DeviceEntity::class],
)
internal abstract class SafeTravelDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao

    companion object {
        const val DATABASE_FILE = "safe_travel.db"
    }
}

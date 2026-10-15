package com.rizqinh.locpuskesmas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PuskesmasEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun puskesmasDao(): PuskesmasDao
}
package com.rizqinh.locpuskesmas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PuskesmasDao {
    @Query("SELECT * FROM table_puskesmas")
    fun getAllPuskesmas(): Flow<List<PuskesmasEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPuskesmasList(puskesmasList: List<PuskesmasEntity>)
    
    @Query("DELETE FROM table_puskesmas")
    suspend fun clearAll()
}
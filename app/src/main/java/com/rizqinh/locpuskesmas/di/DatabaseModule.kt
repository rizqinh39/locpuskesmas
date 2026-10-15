package com.rizqinh.locpuskesmas.di

import android.content.Context
import androidx.room.Room
import com.rizqinh.locpuskesmas.core.constants.AppConstants
import com.rizqinh.locpuskesmas.data.local.AppDatabase
import com.rizqinh.locpuskesmas.data.local.PuskesmasDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppConstants.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePuskesmasDao(database: AppDatabase): PuskesmasDao {
        return database.puskesmasDao()
    }
}
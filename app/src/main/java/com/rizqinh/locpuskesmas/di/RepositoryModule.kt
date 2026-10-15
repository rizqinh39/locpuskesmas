package com.rizqinh.locpuskesmas.di

import com.rizqinh.locpuskesmas.data.repository.PuskesmasRepositoryImpl
import com.rizqinh.locpuskesmas.domain.repository.PuskesmasRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPuskesmasRepository(
        repositoryImpl: PuskesmasRepositoryImpl
    ): PuskesmasRepository
}
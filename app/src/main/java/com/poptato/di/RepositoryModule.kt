package com.poptato.di

import com.poptato.data.repository.AuthRepositoryImpl
import com.poptato.data.repository.BacklogRepositoryImpl
import com.poptato.domain.repository.AuthRepository
import com.poptato.domain.repository.BacklogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun providesAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun providesBacklogRepository(repositoryImpl: BacklogRepositoryImpl): BacklogRepository
}
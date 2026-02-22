package com.poptato.di

import com.poptato.data.repository.AuthRepositoryImpl
import com.poptato.data.repository.BacklogRepositoryImpl
import com.poptato.data.repository.CategoryRepositoryImpl
import com.poptato.data.repository.HistoryRepositoryImpl
import com.poptato.data.repository.MyPageRepositoryImpl
import com.poptato.data.repository.TodayRepositoryImpl
import com.poptato.data.repository.TodoRepositoryImpl
import com.poptato.data.repository.YesterdayRepositoryImpl
import com.poptato.domain.repository.AuthRepository
import com.poptato.domain.repository.BacklogRepository
import com.poptato.domain.repository.CategoryRepository
import com.poptato.domain.repository.HistoryRepository
import com.poptato.domain.repository.MyPageRepository
import com.poptato.domain.repository.TodayRepository
import com.poptato.domain.repository.TodoRepository
import com.poptato.domain.repository.YesterdayRepository
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

    @Singleton
    @Binds
    abstract fun providesYesterdayRepository(repositoryImpl: YesterdayRepositoryImpl): YesterdayRepository

    @Singleton
    @Binds
    abstract fun providesHistoryRepository(repositoryImpl: HistoryRepositoryImpl): HistoryRepository

    @Singleton
    @Binds
    abstract fun providesTodoRepository(repositoryImpl: TodoRepositoryImpl): TodoRepository

    @Singleton
    @Binds
    abstract fun providesMyPageRepository(repositoryImpl: MyPageRepositoryImpl): MyPageRepository

    @Singleton
    @Binds
    abstract fun providesTodayRepository(repositoryImpl: TodayRepositoryImpl): TodayRepository

    @Singleton
    @Binds
    abstract fun providesCategoryRepository(repositoryImpl: CategoryRepositoryImpl): CategoryRepository
}

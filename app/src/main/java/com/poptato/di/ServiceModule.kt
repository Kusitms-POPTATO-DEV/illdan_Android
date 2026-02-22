package com.poptato.di

import com.poptato.data.service.AuthService
import com.poptato.data.service.BacklogService
import com.poptato.data.service.CategoryService
import com.poptato.data.service.HistoryService
import com.poptato.data.service.TodoService
import com.poptato.data.service.MyPageService
import com.poptato.data.service.YesterdayService
import com.poptato.data.service.TodayService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    fun provideBacklogService(retrofit: Retrofit): BacklogService {
        return retrofit.create(BacklogService::class.java)
    }

    @Provides
    fun provideYesterdayService(retrofit: Retrofit): YesterdayService {
        return retrofit.create(YesterdayService::class.java)
    }

    @Provides
    fun provideHistoryService(retrofit: Retrofit): HistoryService {
        return retrofit.create(HistoryService::class.java)
    }

    @Provides
    fun provideTodoService(retrofit: Retrofit): TodoService {
        return retrofit.create(TodoService::class.java)
    }

    @Provides
    fun provideMyPageService(retrofit: Retrofit): MyPageService {
        return retrofit.create(MyPageService::class.java)
    }

    @Provides
    fun provideTodayService(retrofit: Retrofit): TodayService {
        return retrofit.create(TodayService::class.java)
    }

    @Provides
    fun providesCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }
}

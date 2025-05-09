package com.poptato.di

import com.poptato.ui.viewModel.GuideViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun provideGuideViewModel(): GuideViewModel {
        return GuideViewModel()
    }
}
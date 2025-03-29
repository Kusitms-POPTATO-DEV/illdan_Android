package com.poptato.di

import android.content.Context
import com.poptato.data.datastore.PoptatoDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): PoptatoDataStore {
        return PoptatoDataStore(context)
    }
}
package com.poptato.di

import com.poptato.app.BuildConfig
import com.poptato.data.datastore.PoptatoDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val AUTHORIZATION = "Authorization"

    @Singleton
    @Provides
    fun provideOkHttpClient(
        dataStore: PoptatoDataStore,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val headerInterceptor = Interceptor{ chain ->
            val tokenFlow = dataStore.accessToken
            val token = runBlocking { tokenFlow.first() }
            val request = chain.request().newBuilder()

            token?.let {
                request.addHeader(AUTHORIZATION, "Bearer $it")
            }

            val requestWithHeaders = request.build()
            Timber.i("Request Headers: ${requestWithHeaders.headers}")

            chain.proceed(request.build())
        }

        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
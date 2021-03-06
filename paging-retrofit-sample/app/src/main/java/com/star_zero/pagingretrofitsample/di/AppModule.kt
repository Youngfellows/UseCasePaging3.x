package com.star_zero.pagingretrofitsample.di

import com.star_zero.pagingretrofitsample.HttpLogger
import com.star_zero.pagingretrofitsample.api.GitHubAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideApi(): GitHubAPI {
        val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(HttpLogger()).apply {
                setLevel(HttpLoggingInterceptor.Level.BASIC)
            })
            .build()

        val retrofit = Retrofit.Builder()
            .client(okhttp)
            .baseUrl("https://api.github.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        return retrofit.create(GitHubAPI::class.java)
    }
}

package com.adrena.commerce.paging3.data

import com.adrena.commerce.paging3.data.model.MoviesResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API接口
 */
interface TMDBService {

    @GET("discover/movie?sort_by=original_title.asc&region=ID")
    suspend fun moviesFlow(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): MoviesResponse

    @GET("movie/popular")
    fun popularMovieRx(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Single<MoviesResponse>

    companion object {

        /**
         * 请求Host
         */
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        /**
         * 获取API接口
         * @return
         */
        fun create(): TMDBService {
            val logger = HttpLoggingInterceptor(HttpLogger())
            //logger.level = HttpLoggingInterceptor.Level.BASIC
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TMDBService::class.java)
        }
    }
}
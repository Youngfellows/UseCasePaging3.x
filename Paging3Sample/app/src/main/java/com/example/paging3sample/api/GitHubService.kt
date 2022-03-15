package com.example.paging3sample.api

import com.example.paging3sample.model.RepoResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 网络请求API
 */
interface GitHubService {

    /**
     * 请求分页列表
     * @param page 第x页
     * @param perPage
     * @return
     */
    @GET("search/repositories?sort=stars&q=Android")
    suspend fun searchRepos(@Query("page") page: Int, @Query("per_page") perPage: Int): RepoResponse

    companion object {

        /**
         * 请求Host
         */
        private const val BASE_URL = "https://api.github.com/"

        /**
         * 获取请求接口
         * @return
         */
        fun create(): GitHubService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubService::class.java)
        }

        private fun getHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(getHttpLogger())
                .build()
        }

        private fun getHttpLogger(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor(HttpLogger()).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

}
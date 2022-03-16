package com.example.paging3demo.data.remote

import com.example.paging3demo.BuildConfig
import com.example.paging3demo.model.SearchResult
import com.example.paging3demo.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * 网络接口
 */
interface UnsplashApi {
    /**
     * 查询分页图片
     * @param page 第x页
     * @param perPage 每一页大小
     * @return
     */
    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/photos")
    suspend fun getAllImages(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<UnsplashImage>

    /**
     * 根据关键字查询分页图片
     * @param query 查询关键字
     * @param perPage 每一页大小
     * @return
     */
    @Headers("Authorization: Client-ID ${BuildConfig.API_KEY}")
    @GET("/search/photos")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("per_page") perPage: Int
    ): SearchResult

}
package com.adrena.commerce.paging3.data.model

import com.google.gson.annotations.SerializedName

/**
 * 响应
 * @property total 总页数
 * @property page 当前页
 * @property results 分页列表
 */
data class MoviesResponse(
    @SerializedName("total_pages") val total: Int = 0,
    val page: Int = 0,
    val results: List<Movie>
) {

    /**
     * 电影实体
     * @property popularity
     * @property voteCount
     * @property video
     * @property posterPath
     * @property id
     * @property adult
     * @property backdropPath
     * @property originalLanguage
     * @property originalTitle
     * @property title
     * @property voteAverage
     * @property overview
     * @property releaseDate
     */
    data class Movie(
        val popularity: Double,
        @SerializedName("vote_count") val voteCount: Int,
        val video: Boolean,
        @SerializedName("poster_path") val posterPath: String?,
        val id: Long,
        val adult: Boolean,
        @SerializedName("backdrop_path") val backdropPath: String?,
        @SerializedName("original_language") val originalLanguage: String,
        @SerializedName("original_title") val originalTitle: String,
        val title: String,
        @SerializedName("vote_average") val voteAverage: Double,
        val overview: String,
        @SerializedName("release_date") val releaseDate: String?
    )
}
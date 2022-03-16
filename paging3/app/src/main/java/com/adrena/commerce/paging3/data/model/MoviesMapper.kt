package com.adrena.commerce.paging3.data.model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 数据转化
 */
class MoviesMapper {

    /**
     * 电影数据转化
     * @param response 响应结果
     * @param locale
     * @return 返回包装的电影数据
     */
    @SuppressLint("NewApi")
    fun transform(response: MoviesResponse, locale: Locale): Movies {
        return with(response) {
            Movies(
                total = total,
                page = page,
                movies = results.map {
                    Movies.Movie(
                        0,
                        it.id,
                        it.popularity,
                        it.video,
                        it.posterPath?.let { path -> Image(path) },
                        it.adult,
                        it.backdropPath?.let { path -> Image(path) },
                        it.originalLanguage,
                        it.originalTitle,
                        it.title,
                        it.voteAverage,
                        it.overview,
                        it.releaseDate?.let { date ->
                            if (date.isNotEmpty()) {
                                SimpleDateFormat("YYYY-mm-dd", locale).parse(date)
                            } else {
                                null
                            }
                        }
                    )
                }
            )
        }
    }
}
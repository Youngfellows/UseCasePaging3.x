package com.adrena.commerce.paging3.data.flow

import androidx.paging.PagingData
import com.adrena.commerce.paging3.data.model.Movies
import kotlinx.coroutines.flow.Flow

/**
 * 获取电影被观察分页数据流
 */
interface GetMoviesFlowRepository {

    /**
     * 获取电影被观察分页数据流
     * @return
     */
    fun getMovies(): Flow<PagingData<Movies.Movie>>
}
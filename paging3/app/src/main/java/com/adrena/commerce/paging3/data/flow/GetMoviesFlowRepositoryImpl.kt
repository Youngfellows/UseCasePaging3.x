package com.adrena.commerce.paging3.data.flow

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.adrena.commerce.paging3.data.model.Movies
import kotlinx.coroutines.flow.Flow

/**
 * 获取电影被观察分页数据流
 * @property pagingSource 分页数据源
 */
class GetMoviesFlowRepositoryImpl(private val pagingSource: GetMoviesFlowPagingSource) :
    GetMoviesFlowRepository {

    /**
     * 获取电影被观察分页数据流
     * @return
     */
    override fun getMovies(): Flow<PagingData<Movies.Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = { pagingSource }
        ).flow
    }
}
package com.adrena.commerce.paging3.data.flow

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.adrena.commerce.paging3.data.db.MovieDatabase
import com.adrena.commerce.paging3.data.model.Movies
import kotlinx.coroutines.flow.Flow

/**
 * 获取本地数据源,如果没有本地数据,则加载网络数据并存储
 * @property database 数据库
 * @property remoteMediator 本地数据源
 */
class GetMoviesFlowRemoteRepositoryImpl(
    private val database: MovieDatabase,
    private val remoteMediator: GetMoviesFlowRemoteMediator
) : GetMoviesFlowRepository {

    override fun getMovies(): Flow<PagingData<Movies.Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.moviesFlowDao().selectAll() } //分页数据源
        ).flow
    }
}
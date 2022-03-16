package com.example.paging3demo.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3demo.data.local.UnsplashDatabase
import com.example.paging3demo.data.paging.SearchPagingSource
import com.example.paging3demo.data.paging.UnsplashRemoteMediator
import com.example.paging3demo.data.remote.UnsplashApi
import com.example.paging3demo.model.UnsplashImage
import com.example.paging3demo.util.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 真正执行任务的Repository
 * @property unsplashApi 网络数据源
 * @property unsplashDatabase 本地数据源
 */
@ExperimentalPagingApi
class Repository @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val unsplashDatabase: UnsplashDatabase
) {

    /**
     * 被观察分页网络数据流-获取分页图片
     * @return
     */
    fun getAllImages(): Flow<PagingData<UnsplashImage>> {
        //本地数据源
        val pagingSourceFactory = { unsplashDatabase.unsplashImageDao().getAllImages() }
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            //本地数据源
            remoteMediator = UnsplashRemoteMediator(
                unsplashApi = unsplashApi,
                unsplashDatabase = unsplashDatabase
            ),
            //网络数据源
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * 被观察分页网络数据流-获取被查询分页图片
     * @param query 查询关键字
     * @return
     */
    fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {
                //网络分页数据源
                SearchPagingSource(unsplashApi = unsplashApi, query = query)
            }
        ).flow
    }

}
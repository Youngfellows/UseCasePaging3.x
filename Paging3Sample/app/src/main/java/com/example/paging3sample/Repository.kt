package com.example.paging3sample

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.paging3sample.api.GitHubService
import com.example.paging3sample.model.Repo
import kotlinx.coroutines.flow.Flow

/**
 * 加载数据的Repository
 */
object Repository {

    /**
     * 每页50条数据
     */
    private const val PAGE_SIZE = 50

    /**
     * API请求接口
     */
    private val gitHubService = GitHubService.create()

    /**
     * 获取分页数据PagingData
     * @return
     */
    fun getPagingData(): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(PAGE_SIZE),//分页配置
            pagingSourceFactory = {
                //返回一个分页数据源
                RepoPagingSource(gitHubService)
            }
        ).flow
    }

}
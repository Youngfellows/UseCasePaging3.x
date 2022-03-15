package com.example.paging3sample

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3sample.api.GitHubService
import com.example.paging3sample.model.Repo
import java.lang.Exception

/**
 * 分页请求数据源
 * @property gitHubService API请求接口
 */
class RepoPagingSource(private val gitHubService: GitHubService) : PagingSource<Int, Repo>() {

    companion object {
        private const val TAG = "RepoPagingSource"
    }

    /**
     * 加载分页数据
     * @param params
     * @return
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key ?: 1 // set page 1 as default
            val pageSize = params.loadSize
            Log.d(TAG, "load:: page:${page},pageSize:${pageSize}")
            val repoResponse = gitHubService.searchRepos(page, pageSize)
            val repoItems = repoResponse.items
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        Log.d(TAG, "getRefreshKey:: $state")
        return null
    }

}
package com.star_zero.pagingretrofitsample.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.star_zero.pagingretrofitsample.api.GitHubAPI
import com.star_zero.pagingretrofitsample.data.Repo
import timber.log.Timber

/**
 * 分页数据源
 * @property api API网络接口
 */
class RepoPagingSource(
    private val api: GitHubAPI
) : PagingSource<Int, Repo>() {

    companion object {
        private const val TAG: String = "RepoPagingSource"
    }

    /**
     * 分页加载数据
     * @param params
     * @return
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key ?: 1
            val response = api.repos("google", page, params.loadSize)

            val repos = response.body()
            if (repos != null) {
                var next: Int? = null
                // Check if there is next page
                response.headers()["Link"]?.let { link ->
                    val regex = Regex("rel=\"next\"")
                    if (regex.containsMatchIn(link)) {
                        next = page + 1
                    }
                }
                LoadResult.Page(
                    data = repos,
                    prevKey = null,
                    nextKey = next
                )
            } else {
                Log.d(TAG, "load:: empty ... ")
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "load: error,${e.message}")
            Timber.w(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return null
    }
}

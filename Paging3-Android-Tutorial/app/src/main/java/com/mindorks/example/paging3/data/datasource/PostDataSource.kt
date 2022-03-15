package com.mindorks.example.paging3.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import com.mindorks.example.paging3.data.APIService
import com.mindorks.example.paging3.data.response.Data

/**
 * 分页列表数据源
 * @property apiService API接口
 */
class PostDataSource(private val apiService: APIService) : PagingSource<Int, Data>() {

    companion object {
        private const val TAG = "PostDataSource"
    }

    /**
     * 有bug,一直死循环加载数据
     * 加载首页及下一页
     * @param params
     * @return
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        try {
            //当前页key
            val currentLoadingPageKey = params.key ?: 1
            val response = apiService.getListData(currentLoadingPageKey)
            val responseBody = response.body()
            val responseData = mutableListOf<Data>()
            val data = responseBody?.myData ?: emptyList()
            responseData.addAll(data)

            Log.d(
                TAG,
                "load:: currentLoadingPageKey:$currentLoadingPageKey,pageSize:${params.pageSize},total_pages:${responseBody?.total_pages}"
            )
            if (responseBody?.total_pages!! >= currentLoadingPageKey) {
                //上一页key
                val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
                Log.d(TAG, "load:: response data size: ${responseData.size}")
                if (responseData.isNullOrEmpty()) {
                    Log.d(TAG, "load:: isNullOrEmpty ...")
                    return LoadResult.Error(IllegalArgumentException("response data is null or empty "))
                }
                //加载成功回调
                return LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
                )
            } else {
                return LoadResult.Error(IllegalArgumentException("response data is null or empty xxx"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "load:: error," + e.message)
            //加载异常回调
            return LoadResult.Error(e)
        }
    }

}
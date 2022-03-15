package com.star_zero.pagingretrofitsample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.star_zero.pagingretrofitsample.api.GitHubAPI
import com.star_zero.pagingretrofitsample.paging.RepoPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 可被观察数据的ViewModel
 * @property api API网络接口
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: GitHubAPI
) : ViewModel() {

    /**
     * 可被观察分页列表数据流
     */
    val reposFlow = Pager(
        PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE)
    ) {
        RepoPagingSource(api) //分页数据源
    }.flow
        .map { pagingData ->
            pagingData.map {
                UiModel(
                    it.id,
                    it.fullName
                )
            }
        }.cachedIn(viewModelScope)

    companion object {
        /**
         * 每一页的大小
         */
        private const val PAGE_SIZE = 20
    }
}

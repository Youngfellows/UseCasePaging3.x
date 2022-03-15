package com.mindorks.example.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mindorks.example.paging3.data.APIService
import com.mindorks.example.paging3.data.datasource.PostDataSource

/**
 * VM
 * @property apiService API接口
 */
class MainViewModel(private val apiService: APIService) : ViewModel() {

    /**
     * 被观察分页列表数据
     */
    val listData = Pager(PagingConfig(pageSize = 20)) {
        PostDataSource(apiService)
    }.flow.cachedIn(viewModelScope)
}
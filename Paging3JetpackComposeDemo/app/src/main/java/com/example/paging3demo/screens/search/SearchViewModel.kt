package com.example.paging3demo.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3demo.data.repository.Repository
import com.example.paging3demo.model.UnsplashImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 查询图片被观察数据的ViewModel
 * @property repository 正在执行任务的Repository
 */
@ExperimentalPagingApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    /**
     * 被观察数据-查询关键字
     */
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    /**
     * 被观察数据-分页查询数据流
     */
    private val _searchedImages = MutableStateFlow<PagingData<UnsplashImage>>(PagingData.empty())
    val searchedImages = _searchedImages

    /**
     * 设置查询关键字
     * @param query 关键字
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * 异步分页查询-使用协程
     * @param query 查询关键字
     */
    fun searchHeroes(query: String) {
        viewModelScope.launch {
            repository.searchImages(query = query).cachedIn(viewModelScope).collect {
                _searchedImages.value = it
            }
        }
    }

}
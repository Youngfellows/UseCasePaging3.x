package com.adrena.commerce.paging3.view.viewmodel.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adrena.commerce.paging3.data.flow.GetMoviesFlowRepository

/**
 * 返回携带参数的ViewModel
 * @property repository 电影被观察分页数据流
 */
class GetMoviesFlowViewModelFactory(private val repository: GetMoviesFlowRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GetMoviesFlowViewModel::class.java)) {
            return GetMoviesFlowViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
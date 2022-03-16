package com.example.paging3demo.screens.home

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.example.paging3demo.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 首页被观察数据的ViewModel
 * @constructor
 * TODO
 *
 * @param repository
 */
@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    /**
     * 被观察分页网络数据流
     */
    val getAllImages = repository.getAllImages()
}
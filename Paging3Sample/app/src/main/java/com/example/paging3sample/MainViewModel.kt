package com.example.paging3sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paging3sample.model.Repo
import kotlinx.coroutines.flow.Flow

/**
 * 提供被观察数据的VM
 */
class MainViewModel : ViewModel() {

    /**
     * 获取被观察的分页数据流
     * @return
     */
    fun getPagingData(): Flow<PagingData<Repo>> {
        return Repository.getPagingData().cachedIn(viewModelScope)
    }

}
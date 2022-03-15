/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.android.codelabs.paging.data.GithubRepository
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.example.android.codelabs.paging.model.UiAction
import com.example.android.codelabs.paging.model.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
class SearchRepositoriesViewModel(
    private val repository: GithubRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Stream of immutable states representative of the UI.
     * UI响应结果
     */
    val state: LiveData<UiState>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     * 密封类常量-UI行为
     */
    val accept: (UiAction) -> Unit

    init {
        //被观察查询关键字
        val queryLiveData =
            MutableLiveData(savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY)

        //查询触发
        state = queryLiveData
            .distinctUntilChanged()
            .switchMap { queryString ->
                liveData {
                    val uiState = repository.getSearchResultStream(queryString)
                        .map {
                            UiState(
                                query = queryString,
                                searchResult = it
                            )
                        }
                        .asLiveData(Dispatchers.Main)
                    emitSource(uiState)
                }
            }

        /**
         * 定义UI行为回调
         */
        accept = { action ->
            when (action) {
                //查询
                is UiAction.Search -> queryLiveData.postValue(action.query)
                //滚动
                is UiAction.Scroll -> if (action.shouldFetchMore) {
                    val immutableQuery = queryLiveData.value
                    if (immutableQuery != null) {
                        viewModelScope.launch {
                            repository.requestMore(immutableQuery)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value?.query
        super.onCleared()
    }
}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount


private const val VISIBLE_THRESHOLD = 5
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = "Android"
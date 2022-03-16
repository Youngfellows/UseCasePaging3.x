package com.adrena.commerce.paging3.view.viewmodel.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.adrena.commerce.paging3.data.flow.GetMoviesFlowRepository
import com.adrena.commerce.paging3.view.model.UiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * 携带被观察数据的ViewModel
 * @property repository 电影被观察分页数据流
 */
class GetMoviesFlowViewModel(private val repository: GetMoviesFlowRepository) : ViewModel() {

    /**
     * 获取电影被观察分页数据流
     * @return
     */
    fun getFavoriteMovies(): Flow<PagingData<UiModel>> {
        return repository
            .getMovies()
            .map { pagingData ->
                //数据转化
                pagingData.map {
                    UiModel.MovieItem(it)
                }
            }
            .map {
                it.insertSeparators<UiModel.MovieItem, UiModel> { before, after ->
                    if (after == null) {
                        return@insertSeparators null
                    }

                    val alphabet = after.movie.originalTitle.replace("The", "").trim().take(1)

                    if (before == null) {
                        return@insertSeparators UiModel.SeparatorItem(alphabet)
                    }

                    if (before.movie.originalTitle.replace("The", "").trim().take(1) != alphabet) {
                        UiModel.SeparatorItem(alphabet)
                    } else {
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
    }
}
package com.adrena.commerce.paging3.view.model

import com.adrena.commerce.paging3.data.model.Movies

/**
 * 密封类-UI数据实体
 */
sealed class UiModel {

    /**
     * 电影
     * @property movie 电影列表
     */
    data class MovieItem(val movie: Movies.Movie) : UiModel()

    data class SeparatorItem(val description: String) : UiModel()
}
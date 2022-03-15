package com.star_zero.pagingretrofitsample.ui

/**
 * Item数据实体
 * @property id
 * @property fullName
 * @property favorite
 */
data class UiModel(
    val id: String,
    val fullName: String,
    var favorite: Boolean = false
)

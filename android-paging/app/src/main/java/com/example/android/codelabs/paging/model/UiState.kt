package com.example.android.codelabs.paging.model

/**
 * 拼装UI响应结果
 * @property query 查询关键字
 * @property searchResult 请求结果
 */
data class UiState(
    val query: String,
    val searchResult: RepoSearchResult
)
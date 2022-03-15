package com.example.android.codelabs.paging.model

/**
 * 密封类常量-UI行为
 */
sealed class UiAction {

    /**
     * 搜索行为
     */
    data class Search(val query: String) : UiAction()


    /**
     *  滚动行为
     * @property visibleItemCount 可见item个数
     * @property lastVisibleItemPosition 最后一个item位置
     * @property totalItemCount item总个数
     */
    data class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiAction()
}

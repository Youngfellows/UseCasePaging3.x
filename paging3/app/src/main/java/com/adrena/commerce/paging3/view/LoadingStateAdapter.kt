package com.adrena.commerce.paging3.view

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.adrena.commerce.paging3.view.viewholder.LoadingStateViewHolder

/**
 * 分页列表的Footer
 */
class LoadingStateAdapter : LoadStateAdapter<LoadingStateViewHolder>() {

    /**
     * 为footer绑定数据
     * @param holder 视图
     * @param loadState 加载状态
     */
    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    /**
     * 创建分页列表的Footer
     * @param parent
     * @param loadState 加载状态
     * @return
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        return LoadingStateViewHolder.create(parent)
    }
}
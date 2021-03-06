package com.adrena.commerce.paging3.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.adrena.commerce.paging3.R
import com.adrena.commerce.paging3.databinding.LoadingItemBinding

/**
 * 分页列表的Footer
 * @property binding
 */
class LoadingStateViewHolder(
    private val binding: LoadingItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * 为视图绑定数据
     * @param loadState
     */
    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
    }

    companion object {

        /**
         * 创建ViewHolder视图
         * @param parent
         * @return
         */
        fun create(parent: ViewGroup): LoadingStateViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.loading_item, parent, false)

            val binding = LoadingItemBinding.bind(view)

            return LoadingStateViewHolder(
                binding
            )
        }
    }
}
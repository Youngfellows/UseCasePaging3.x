package com.adrena.commerce.paging3.view.flow

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adrena.commerce.paging3.R
import com.adrena.commerce.paging3.view.model.UiModel
import com.adrena.commerce.paging3.view.viewholder.MovieViewHolder
import com.adrena.commerce.paging3.view.viewholder.SeparatorViewHolder
import java.util.*

/**
 * 电影分页列表适配器
 * @property locale
 */
class MoviesFlowAdapter(private val locale: Locale) :
    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(
        COMPARATOR
    ) {

    /**
     * 创建与视图绑定的ViewHolder
     * @param parent
     * @param viewType 条目类型
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.movie_item) {
            MovieViewHolder.create(
                parent,
                locale
            )
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    /**
     * 获取条目类型
     * @param position 位置
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.MovieItem -> R.layout.movie_item //电影类型
            is UiModel.SeparatorItem -> R.layout.separator_item //分割符类型
            null -> throw UnsupportedOperationException("Unknown View")
        }
    }

    /**
     * 为视图绑定数据
     * @param holder 绑定视图的viewHolder
     * @param position 位置
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is UiModel.MovieItem -> (holder as MovieViewHolder).bind(it.movie)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(it.description)
            }
        }
    }

    companion object {

        private val COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {

            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.MovieItem && newItem is UiModel.MovieItem && oldItem.movie.movieId == newItem.movie.movieId) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem && oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}

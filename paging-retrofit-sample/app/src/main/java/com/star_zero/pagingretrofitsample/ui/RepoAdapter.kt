package com.star_zero.pagingretrofitsample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.star_zero.pagingretrofitsample.databinding.ItemRepoBinding

/**
 * RV分页数据列表适配器
 */
class RepoAdapter : PagingDataAdapter<UiModel, RepoAdapter.ViewHolder>(DIFF_CALLBACK) {

    /**
     * 创建与视图关联的ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * 为视图绑定数据
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(getItem(position)) {
            model?.let {
                it.favorite = !it.favorite
                notifyItemChanged(position)
            }
        }
    }

    class ViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * 为视图绑定数据和设置点击事件
         * @param model
         * @param onClickFav 点击事件回调
         */
        fun bind(model: UiModel?, onClickFav: () -> Unit) {
            binding.model = model
            if (model == null) {
                binding.buttonFav.setOnClickListener(null)
            } else {
                binding.buttonFav.setOnClickListener {
                    onClickFav()
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}


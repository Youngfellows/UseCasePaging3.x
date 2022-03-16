package com.adrena.commerce.paging3.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adrena.commerce.paging3.R
import com.adrena.commerce.paging3.databinding.SeparatorItemBinding

/**
 * 分割类型ViewHolder
 * @property binding
 */
class SeparatorViewHolder(private val binding: SeparatorItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * 绑定数据
     * @param description
     */
    fun bind(description: String) {
        binding.separatorDescription.text = description
    }

    companion object {

        fun create(parent: ViewGroup): SeparatorViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.separator_item, parent, false)

            val binding = SeparatorItemBinding.bind(view)

            return SeparatorViewHolder(
                binding
            )
        }
    }
}
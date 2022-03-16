package com.adrena.commerce.paging3.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.adrena.commerce.paging3.R
import com.adrena.commerce.paging3.data.model.Movies
import com.adrena.commerce.paging3.databinding.MovieItemBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * 电影类型ViewHolder
 * @property binding
 * @property locale
 */
class MovieViewHolder(private val binding: MovieItemBinding, private val locale: Locale) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * 时间日期格式化
     */
    private val mDateFormatter = SimpleDateFormat("dd MMM yyyy", locale)

    /**
     * 为视图绑定数据
     * @param movie 电影数据
     */
    fun bind(movie: Movies.Movie) {
        with(movie) {
            binding.title.text = originalTitle
            binding.date.text = releaseDate?.let { mDateFormatter.format(it) }
            binding.poster.load(poster?.small) {
                crossfade(true)
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup, locale: Locale): MovieViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_item, parent, false)

            val binding = MovieItemBinding.bind(view)

            return MovieViewHolder(
                binding,
                locale
            )
        }
    }
}

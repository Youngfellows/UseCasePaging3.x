package com.adrena.commerce.paging3.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrena.commerce.paging3.R
import com.adrena.commerce.paging3.Injection
import com.adrena.commerce.paging3.databinding.FragmentMovieListBinding
import com.adrena.commerce.paging3.view.flow.MoviesFlowAdapter
import com.adrena.commerce.paging3.view.viewmodel.flow.GetMoviesFlowViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Flow页面
 */
class MovieFlowFragment : Fragment() {

    /**
     * 与视图绑定数据的DataBinding
     */
    private lateinit var mBinding: FragmentMovieListBinding

    /**
     * 携带被观察数据的ViewModel
     */
    private lateinit var mViewModel: GetMoviesFlowViewModel

    /**
     * 分页列表适配器
     */
    private lateinit var mAdapter: MoviesFlowAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMovieListBinding.inflate(inflater, container, false)

        val view = mBinding.root //根布局

        activity?.title = getString(R.string.kotlin_flow_with_paging_source)

        mViewModel = ViewModelProvider(this, Injection.provideFlowViewModel(view.context)).get(
            GetMoviesFlowViewModel::class.java
        )

        val decoration = DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL)

        mAdapter =
            MoviesFlowAdapter(Injection.provideLocale())

        mBinding.list.layoutManager = LinearLayoutManager(view.context)
        //添加footer
        mBinding.list.adapter = mAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter()
        )

        //监听加载状态
        mAdapter.addLoadStateListener { loadState ->
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error

            //错误状态
            errorState?.let {
                AlertDialog.Builder(view.context)
                    .setTitle(R.string.error)
                    .setMessage(it.error.localizedMessage)
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.retry) { _, _ ->
                        //重新加载
                        mAdapter.retry()
                    }
                    .show()
            }
        }

        mBinding.list.addItemDecoration(decoration)

        //异步加载分页数据流
        lifecycleScope.launch {
            mViewModel.getFavoriteMovies().collectLatest {
                mAdapter.submitData(it)
            }
        }

        return view
    }
}
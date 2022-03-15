package com.star_zero.pagingretrofitsample.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.star_zero.pagingretrofitsample.R
import com.star_zero.pagingretrofitsample.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainActivity"
    }

    /**
     * 与界面绑定的DataBinding
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * 被观察是否在刷新数据
     */
    val isRefreshing = MutableLiveData<Boolean>()

    /**
     * 被观察的是否十错误状态数据
     */
    val isError = MutableLiveData<Boolean>()

    /**
     * 被观察数据的VM
     */
    private val viewModel: MainViewModel by viewModels()

    /**
     * RV分页数据列表适配器
     */
    private val adapter = RepoAdapter()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.activity = this

        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        // disable animation
        (binding.recycler.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false

        binding.recycler.adapter = adapter.withLoadStateFooter(RepoLoadStateAdapter(adapter::retry))

        /**
         * 观察分页数据变化
         */
        lifecycleScope.launch {
            viewModel.reposFlow.collectLatest {
                adapter.submitData(it)
            }
        }

        /**
         * 观察加载状态变化
         */
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                Log.d(TAG, "state:: ${loadState.refresh}")
                isRefreshing.value = loadState.refresh is LoadState.Loading
                isError.value = loadState.refresh is LoadState.Error
                when (loadState.refresh) {
                    is LoadState.NotLoading -> {
                        Log.d(TAG, "NotLoading ...")
                    }
                }
            }
        }
    }

    /**
     * 刷新RV
     */
    fun onRefresh() {
        adapter.refresh()
    }

    /**
     * 重试RV
     */
    fun retry() {
        adapter.retry()
    }
}

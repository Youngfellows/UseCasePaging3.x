/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.android.codelabs.paging.Injection
import com.example.android.codelabs.paging.databinding.ActivitySearchRepositoriesBinding
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.example.android.codelabs.paging.model.UiAction
import com.example.android.codelabs.paging.model.UiState

/**
 * 搜索github项目代码仓库界面
 */
class SearchRepositoriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchRepositoriesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // get the view model
        val viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(owner = this))
            .get(SearchRepositoriesViewModel::class.java)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        // bind the state
        binding.bindState(
            uiState = viewModel.state,
            uiActions = viewModel.accept
        )
    }

    /**
     * Binds the [UiState] provided  by the [SearchRepositoriesViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun ActivitySearchRepositoriesBinding.bindState(
        uiState: LiveData<UiState>,
        uiActions: (UiAction) -> Unit
    ) {
        val repoAdapter = ReposAdapter()
        list.adapter = repoAdapter

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            repoAdapter = repoAdapter,
            uiState = uiState,
            onScrollChanged = uiActions
        )
    }

    /**
     * 为搜索框绑定数据
     * @param uiState
     * @param onQueryChanged 查询回调
     */
    private fun ActivitySearchRepositoriesBinding.bindSearch(
        uiState: LiveData<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        //观察查询关键字变化
        uiState
            .map(UiState::query)
            .distinctUntilChanged()
            .observe(this@SearchRepositoriesActivity, searchRepo::setText)

//        uiState
//            .map { it.query }
//            .distinctUntilChanged()
//            .observe(this@SearchRepositoriesActivity, Observer {
//                searchRepo.setText(it)
//            })
//
//        uiState
//            .map { it.query }
//            .distinctUntilChanged()
//            .observe(this@SearchRepositoriesActivity, object : Observer<String> {
//                override fun onChanged(t: String?) {
//                    searchRepo.setText(t)
//                }
//            })
    }

    private fun ActivitySearchRepositoriesBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        searchRepo.text.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    /**
     * 为RV列表绑定数据
     * @param repoAdapter RV列表适配器
     * @param uiState
     * @param onScrollChanged 滚动状态回调
     */
    private fun ActivitySearchRepositoriesBinding.bindList(
        repoAdapter: ReposAdapter,
        uiState: LiveData<UiState>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        //设置RV滚动事件监听
        setupScrollListener(onScrollChanged)

        //观察查询结果数据变化
        uiState
            .map(UiState::searchResult)
            //.map { it.searchResult }
            .distinctUntilChanged()
            .observe(this@SearchRepositoriesActivity) { result ->
                when (result) {
                    is RepoSearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        repoAdapter.submitList(result.data)
                    }
                    is RepoSearchResult.Error -> {
                        Toast.makeText(
                            this@SearchRepositoriesActivity,
                            "\uD83D\uDE28 Wooops $result.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun ActivitySearchRepositoriesBinding.showEmptyList(show: Boolean) {
        emptyList.isVisible = show
        list.isVisible = !show
    }

    /**
     * 设置RV滚动事件监听
     * @param onScrollChanged 滚动回调
     */
    private fun ActivitySearchRepositoriesBinding.setupScrollListener(
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        val layoutManager = list.layoutManager as LinearLayoutManager
        list.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount//item条目总数
                val visibleItemCount = layoutManager.childCount//可见条目
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()//最后一个条目

                //回调出去
                onScrollChanged(
                    UiAction.Scroll(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )
            }
        })
    }
}

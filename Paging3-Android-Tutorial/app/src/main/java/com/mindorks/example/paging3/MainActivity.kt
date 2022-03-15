package com.mindorks.example.paging3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.example.paging.R
import com.mindorks.example.paging3.adapter.MainListAdapter
import com.mindorks.example.paging3.data.APIService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
    }

    lateinit var viewModel: MainViewModel
    lateinit var mainListAdapter: MainListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupList()
        setupView()
    }

    private fun setupView() {
        lifecycleScope.launch {
            viewModel.listData.collect {
                Log.d(TAG, "setupView:: submitData")
                mainListAdapter.submitData(it)
            }
        }
    }

    private fun setupList() {
        mainListAdapter = MainListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mainListAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(APIService.getApiService())
        )[MainViewModel::class.java]

//        ViewModelProvider(
//            this,
//            MainViewModelFactory(APIService.getApiService())
//        ).get(MainViewModel::class.java)

    }


}
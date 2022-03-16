package com.adrena.commerce.paging3

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.adrena.commerce.paging3.data.TMDBService
import com.adrena.commerce.paging3.data.db.MovieDatabase
import com.adrena.commerce.paging3.data.flow.GetMoviesFlowPagingSource
import com.adrena.commerce.paging3.data.flow.GetMoviesFlowRemoteMediator
import com.adrena.commerce.paging3.data.flow.GetMoviesFlowRemoteRepositoryImpl
import com.adrena.commerce.paging3.data.flow.GetMoviesFlowRepositoryImpl
import com.adrena.commerce.paging3.data.model.MoviesMapper
import com.adrena.commerce.paging3.data.rx.GetMoviesRxPagingSource
import com.adrena.commerce.paging3.data.rx.GetMoviesRxRemoteMediator
import com.adrena.commerce.paging3.data.rx.GetMoviesRxRemoteRepositoryImpl
import com.adrena.commerce.paging3.data.rx.GetMoviesRxRepositoryImpl
import com.adrena.commerce.paging3.view.viewmodel.flow.GetMoviesFlowViewModelFactory
import com.adrena.commerce.paging3.view.viewmodel.rx.GetMoviesRxViewModelFactory
import java.util.*

/**
 * 注入对象
 */
object Injection {

    fun provideLocale(): Locale = Locale.getDefault()

    /**
     * 本地数据库
     * @param context
     * @return
     */
    fun provideDatabase(context: Context): MovieDatabase = MovieDatabase.getInstance(context)

    /**
     * 注入ViewModel,ViewModel关联对象
     * @param context
     * @return
     */
    fun provideFlowViewModel(context: Context): ViewModelProvider.Factory {

        //分页数据源
        val pagingSource = GetMoviesFlowPagingSource(
            service = TMDBService.create(),
            apiKey = context.getString(R.string.api_key),
            mapper = MoviesMapper(),
            locale = provideLocale()
        )

        //获取电影被观察分页数据流
        val repository = GetMoviesFlowRepositoryImpl(
            pagingSource = pagingSource
        )

        return GetMoviesFlowViewModelFactory(
            repository
        )
    }

    /**
     * 注入ViewModel,ViewModel关联对象,本地数据源和网络数据源
     * @param context
     * @return
     */
    fun provideFlowRemoteViewModel(context: Context): ViewModelProvider.Factory {

        //本地数据源
        val remoteMediator = GetMoviesFlowRemoteMediator(
            service = TMDBService.create(),
            database = provideDatabase(context),
            apiKey = context.getString(R.string.api_key),
            mapper = MoviesMapper(),
            locale = provideLocale()
        )

        val repository = GetMoviesFlowRemoteRepositoryImpl(
            database = provideDatabase(context),
            remoteMediator = remoteMediator
        )

        return GetMoviesFlowViewModelFactory(
            repository
        )
    }

    /**
     * 注入ViewModel,ViewModel关联对象,分页数据源
     * @param context
     * @return
     */
    fun provideRxViewModel(context: Context): ViewModelProvider.Factory {
        //使用rxjava异步加载分页数据源
        val pagingSource = GetMoviesRxPagingSource(
            service = TMDBService.create(),
            apiKey = context.getString(R.string.api_key),
            mapper = MoviesMapper(),
            locale = provideLocale()
        )

        val repository = GetMoviesRxRepositoryImpl(
            pagingSource = pagingSource
        )

        return GetMoviesRxViewModelFactory(
            repository
        )
    }


    /**
     * 注入ViewModel,ViewModel关联对象,分页数据源
     * @param context
     * @return
     */
    fun provideRxRemoteViewModel(context: Context): ViewModelProvider.Factory {

        val remoteMediator = GetMoviesRxRemoteMediator(
            service = TMDBService.create(),
            database = provideDatabase(context),
            apiKey = context.getString(R.string.api_key),
            mapper = MoviesMapper(),
            locale = provideLocale()
        )

        val repository = GetMoviesRxRemoteRepositoryImpl(
            database = provideDatabase(context),
            remoteMediator = remoteMediator
        )

        return GetMoviesRxViewModelFactory(
            repository
        )
    }
}
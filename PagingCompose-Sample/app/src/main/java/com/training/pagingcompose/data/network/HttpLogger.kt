package com.training.pagingcompose.data.network

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 网络请求日志拦截器
 */
class HttpLogger : HttpLoggingInterceptor.Logger {

    companion object {
        private const val TAG: String = "HttpLogger"
    }

    override fun log(message: String) {
        Log.w(TAG, "$message")
    }
}
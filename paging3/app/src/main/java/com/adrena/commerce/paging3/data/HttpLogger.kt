package com.adrena.commerce.paging3.data

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger : HttpLoggingInterceptor.Logger {

    companion object {
        private const val TAG: String = "HttpLogger"
    }

    override fun log(message: String) {
        Log.w(TAG, "$message")
    }
}
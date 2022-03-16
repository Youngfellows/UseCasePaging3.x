/*
 * MIT License
 *
 * Copyright (c) 2020 Vivek Singh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.training.pagingcompose.di

import android.util.Log
import com.training.pagingcompose.BuildConfig
import com.training.pagingcompose.data.network.HttpLogger
import com.training.pagingcompose.data.network.MovieApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG: String = "NetworkModule"

/**
 * 网络API模块
 */
val networkModule = module {
    single { headerInterceptor() }
    single { httpLogger() }
    single { okhttpClient(get(), get()) }
//    single { okhttpClient(get()) }
    single { retrofit(get()) }
    single { apiService(get()) }
}

/**
 * 返回API接口
 * @param retrofit
 * @return
 */
fun apiService(
    retrofit: Retrofit
): MovieApi =
    retrofit.create(MovieApi::class.java)

/**
 * 返回Retrofit
 * @param okHttpClient
 * @return
 */
fun retrofit(
    okHttpClient: OkHttpClient
): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

/**
 * 添加okHttpClient
 * @param headerInterceptor
 * @param httpLoggerInterceptor
 * @return
 */
fun okhttpClient(
    headerInterceptor: Interceptor,
    httpLoggerInterceptor: HttpLoggingInterceptor
): OkHttpClient =
    OkHttpClient.Builder()
        //.addNetworkInterceptor(httpLoggerInterceptor)
        .addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggerInterceptor)
        .build()

/**
 * 添加okHttpClient
 * @param httpLoggerInterceptor
 * @return
 */
fun okhttpClient(
    httpLoggerInterceptor: HttpLoggingInterceptor
): OkHttpClient =
    OkHttpClient.Builder()
        //.addNetworkInterceptor(httpLoggerInterceptor)
        //.addInterceptor(headerInterceptor)
        .addInterceptor(httpLoggerInterceptor)
        .build()

/**
 * 添加公共的请求头
 * @return
 */
fun headerInterceptor(): Interceptor =
    Interceptor { chain ->
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        Log.d(TAG, "headerInterceptor:: newReques=$newRequest")
        chain.proceed(newRequest)
    }

//fun headerInterceptor2(): Interceptor = object : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val newUrl = request.url.newBuilder()
//            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
//            .build()
//
//        val newRequest = request.newBuilder()
//            .url(newUrl)
//            .build()
//        return chain.proceed(newRequest)
//    }
//}

/**
 * 日志拦截器
 * @return
 */
fun httpLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor(HttpLogger()).apply {
    level = HttpLoggingInterceptor.Level.BODY
}

package com.module.network.interceptor

import com.module.network.util.isNetworkAvailable
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 网络请求拦截器 - 缓存
 */
class CacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // 有网络时 设置缓存超时时间1个小时
        val maxAge = 60 * 60
        // 无网络时，设置超时为1天
        val maxStale = 60 * 60 * 24
        var request = chain.request()
        request = if (isNetworkAvailable()) {
            //有网络时只从网络获取
            request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
        } else {
            //无网络时只从缓存中读取
            request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        }


        var response = chain.proceed(request)
        response = if (isNetworkAvailable()) {
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
        } else {
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
        }
        return response
    }
}
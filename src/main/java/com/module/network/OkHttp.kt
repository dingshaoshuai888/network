package com.module.network

import com.module.network.NetworkLib.Companion.DEBUG
import com.module.network.NetworkLib.Companion.interceptorList
import com.module.network.NetworkLib.Companion.mContext
import com.module.network.https.HttpsUtils
import com.module.network.interceptor.CacheInterceptor
import com.module.network.interceptor.LogInterceptor
import com.module.network.util.isExistAsset
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

internal val okHttpClient: OkHttpClient by lazy {

    OkHttpClient.Builder().apply {
        //设置超时时间
        connectTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        //设置Http缓存
//        val cache = Cache(File(mContext.cacheDir, "HttpCache"), (1024 * 1024 * 10).toLong())
//        cache(cache)
        //Header参数拦截器
        interceptorList?.let {
            for (interceptor in it) {
                addInterceptor(interceptor)
            }
        }
        //日志拦截器
        if (DEBUG) {
            addInterceptor(LogInterceptor())
        }
        //缓存拦截器
        addNetworkInterceptor(CacheInterceptor())
        //设置连接池
        connectionPool(ConnectionPool(5, 1, TimeUnit.SECONDS))
        //默认重试一次，若需要重试N次，则要实现拦截器
        retryOnConnectionFailure(true)
        //设置证书
        if (isExistAsset("cert.cer")) {
            val array = arrayOf(mContext.resources.assets.open("cert.cer"))
            val sslParams = HttpsUtils.getSslSocketFactory(array, null, null)

            protocols(Collections.singletonList(Protocol.HTTP_1_1))
            sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        }
    }.build()
}

package com.module.network

import com.module.network.adapter.CoroutineCallAdapterFactory
import com.module.network.adapter.ResponseConverterFactory
import com.module.network.util.isNetworkAvailable
import com.module.network.util.logE
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.ConnectException

/**
 * @author: Xiao Bo
 * @date: 18/7/2020
 */

/**
 * 存放 Api 实例
 */
private val apiMap: HashMap<String, Any> = HashMap()

class RetrofitClient {
    companion object {
        /**
         * 获取 Api 实例，如果缓存里没有，则创建
         */
        fun <S> createService(baseUrl: String = NetworkLib.BASE_URL, cls: Class<S>): S {
            val key = cls.name + baseUrl
            return if (apiMap.containsKey(key)) {
                apiMap[key] as S
            } else {
                val api = createRetrofit(baseUrl).create(cls)
                apiMap[key] = api as Any
                api
            }
        }

        inline fun sendRequest(block: () -> Unit) {
            try {
                block()
            } catch (e: HttpException) {
                logE("错误码: ${e.code()}  错误信息: ${e.message()}")
            }
        }

        inline fun <T> sendRequestForReturn(block: () -> T): T? {
            return try {
                if (isNetworkAvailable()) {
                    block()
                } else {
                    return null
                }
            } catch (e: HttpException) {
                logE("错误码: ${e.code()}  错误信息: ${e.message()}")
                null
            } catch (e: ConnectException) {
                logE("发生了错误，错误信息: ${e.message}")
                null
            } catch (e: Exception) {
                logE("发生了错误，错误信息: ${e.message}")
                null
            }
        }
    }
}

/**
 * 创建 Retrofit
 */
private fun createRetrofit(baseUrl: String) = Retrofit.Builder().apply {
    baseUrl(baseUrl)  // 设置服务器路径
    client(okHttpClient)  // 设置okhttp的网络请求
    addConverterFactory(ResponseConverterFactory.create())// 添加转化库,默认是Gson
    addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //添加回调库，采用RxJava
    addCallAdapterFactory(CoroutineCallAdapterFactory())//添加会调库，适用kotlin协程
}.build()


package com.module.network.util

import com.google.gson.JsonParseException
import com.module.network.NetworkLib.Companion.mContext
import com.module.network.R
import org.json.JSONException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

/**
 * @author: Xiao Bo
 * @date: 18/7/2020
 */
/**
 * 根据错误类型返回提示语
 */
fun getNetworkExceptionMessage(e: Throwable): String {
    if (!isNetworkAvailable()) {
        //网络不可用
        return mContext.getString(R.string.warn_net_disconnect)
    }
    return when (e) {
        //网络不可用
        is UnknownHostException -> mContext.getString(R.string.warn_net_disconnect)
        //网络异常
        is SocketException -> mContext.getString(R.string.warn_net_exception)
        is IOException -> mContext.getString(R.string.network_is_exception)
        //请求超时
        is TimeoutException, is SocketTimeoutException -> mContext.getString(R.string.warn_request_timeout)
        //数据解析失败
        is JsonParseException, is ParseException, is JSONException -> mContext.getString(R.string.warn_data_parse_failed)
        //网络异常
        else -> mContext.getString(R.string.warn_net_exception)
    }
}
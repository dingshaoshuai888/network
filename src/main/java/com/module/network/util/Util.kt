package com.module.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.module.network.NetworkLib.Companion.mContext
import java.io.IOException

/**
 * @author: Xiao Bo
 * @date: 18/7/2020
 */
/**
 * 检查是否有网络
 */
fun isNetworkAvailable(): Boolean {
    val info = getNetworkInfo()
    return info != null && info.isAvailable
}


/**
 * 检查是否是WIFI
 */
fun isWifi(): Boolean {
    val info = getNetworkInfo()
    return if (info != null) {
        info.type == ConnectivityManager.TYPE_WIFI
    } else false
}


/**
 * 检查是否是移动网络
 */
fun isMobile(): Boolean {
    val info = getNetworkInfo()
    return if (info != null) {
        info.type == ConnectivityManager.TYPE_MOBILE
    } else false
}

fun getNetType() = when {
    isMobile() -> "4G"
    isWifi() -> "wifi"
    else -> ""
}

private fun getNetworkInfo(): NetworkInfo? {
    val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo
}

fun isExistAsset(fileName: String): Boolean {
    val assetManager = mContext.assets ?: return false
    try {
        val names = assetManager.list("") ?: return false
        for (i in names.indices) {
            if (fileName.trim { it <= ' ' } == names[i]) {
                return true
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}



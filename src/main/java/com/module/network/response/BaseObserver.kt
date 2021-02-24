package com.module.network.response

import android.text.TextUtils
import com.module.network.util.getNetworkExceptionMessage
import com.module.network.util.logE
import io.reactivex.observers.DisposableObserver

/**
 * @author: Xiao Bo
 * @date: 18/7/2020
 */
abstract class BaseObserver<T> : DisposableObserver<BaseResponse<T>>() {
    override fun onNext(value: BaseResponse<T>) {
        if (value.isSuccess) {
            try {
                onResponseSuccess(value.data)
            } catch (e: Exception) {
                logE("网络解析出现了错误，e:" + e.message)
            }
        } else {
            if (value.code == null) {
                onResponseFailure(msg = value.msg)
                return
            }
            if (TextUtils.isDigitsOnly(value.code)) {
                onResponseFailure(value.code.toInt(), value.msg)
            } else {
                onResponseFailure(msg = value.msg)
            }
        }
    }

    override fun onError(e: Throwable) {
        onResponseFailure(msg = getNetworkExceptionMessage(e))
    }

    override fun onComplete() {}

    protected abstract fun onResponseSuccess(data: T?)
    protected abstract fun onResponseFailure(code: Int = -1, msg: String?)
}
package com.module.network.response

import com.module.network.util.getNetworkExceptionMessage
import io.reactivex.observers.DisposableObserver

/**
 * @author: Xiao Bo
 * @date: 18/7/2020
 */
abstract class ObserverImpl<T> : DisposableObserver<T>() {
    override fun onError(e: Throwable) {
        onResponseFailure(msg = getNetworkExceptionMessage(e))
    }

    override fun onComplete() {}

    protected abstract fun onResponseSuccess(data: T?)
    protected abstract fun onResponseFailure(code: Int = -1, msg: String?)

    override fun onNext(t: T) {
        onResponseSuccess(t)
    }
}
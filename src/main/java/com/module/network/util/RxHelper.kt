package com.module.network.util

import android.content.Context
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.components.RxFragment
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragmentActivity
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * @author: Xiao Bo
 * @date: 18/7/2020
 */
fun <T> observableIO2Main(context: Context): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        val observable = upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        composeContext(context, observable)
    }
}

fun <T> observableIO2Main(fragment: RxFragment): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(fragment.bindToLifecycle())
    }
}

fun <T> flowableIO2Main(): FlowableTransformer<T, T> {
    return FlowableTransformer { upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> changeIOToMainThread(observable: Observable<T>, consumer: DisposableObserver<T>): Disposable {
    return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(consumer)
}

fun <T> composeContext(context: Context, observable: Observable<T>): ObservableSource<T> {
    return when (context) {
        is RxActivity -> {
            observable.compose(context.bindUntilEvent(ActivityEvent.DESTROY))
        }
        is RxFragmentActivity -> {
            observable.compose(context.bindUntilEvent(ActivityEvent.DESTROY))
        }
        is RxAppCompatActivity -> {
            observable.compose(context.bindUntilEvent(ActivityEvent.DESTROY))
        }
        else -> {
            observable
        }
    }
}
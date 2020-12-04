package rxaction

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import log.TAG
import java.util.concurrent.TimeUnit



fun switchMapSample(): Disposable {
    Log.e(TAG, "switch -- map")
    return Observable.interval(500, TimeUnit.MILLISECONDS).switchMap {
        Observable.interval(200, TimeUnit.MILLISECONDS).take(5)
    }.subscribe {
        Log.e(TAG, it.toString())
    }
}

fun concatMapSample(): Disposable{
    Log.e(TAG, "concat -- map")
    return Observable.interval(500, TimeUnit.MILLISECONDS).concatMap {
        Observable.interval(200, TimeUnit.MILLISECONDS).take(5)
    }.subscribe {
        Log.e(TAG, it.toString())
    }
}

fun flatMapSample(): Disposable{
    Log.e(TAG, "flat -- map")
    return Observable.interval(500, TimeUnit.MILLISECONDS).flatMap {
        Observable.interval(200, TimeUnit.MILLISECONDS).take(5)
    }.subscribe {
        Log.e(TAG, it.toString())
    }
}
package rxaction

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import log.TAG
import java.util.concurrent.*

fun concatSample(): Observable<Long> {
    Log.e(TAG, "concat -- map")
    return Observable.concat(
        Observable.intervalRange(
            100,
            3, 2, 1, TimeUnit.SECONDS
        ), Observable.intervalRange(
            200,
            3, 1, 1, TimeUnit.SECONDS
        )
    )
}


fun mergeSample(): Disposable {
    Log.e(TAG, "merge -- map")
    return Observable.merge(
        Observable.intervalRange(
            100,
            3, 2, 1, TimeUnit.SECONDS
        ), Observable.intervalRange(
            200,
            3, 1, 1, TimeUnit.SECONDS
        )
    )
        .subscribe {
            Log.e(TAG, it.toString())
        }
}


fun zipSample(): Disposable {
    Log.e(TAG, "merge -- map")
    return Observable.zip(Observable.intervalRange(
        100,
        3, 2, 1, TimeUnit.SECONDS
    ), Observable.intervalRange(
        200,
        3, 1, 1, TimeUnit.SECONDS
    ), BiFunction<Long, Long, String> { t1, t2 ->
        t1.toString() + t2.toString()
    }).subscribe {
            Log.e(TAG, it.toString())
        }
}


/*
fun concatMapSample(): Disposable {
    Log.e(TAG, "concat -- map")
    return Observable.interval(500, TimeUnit.MILLISECONDS).concatMap {
        Observable.interval(200, TimeUnit.MILLISECONDS).take(5)
    }.subscribe {
        Log.e(TAG, it.toString())
    }
}

fun flatMapSample(): Disposable {
    Log.e(TAG, "flat -- map")
    return Observable.interval(500, TimeUnit.MILLISECONDS).flatMap {
        Observable.interval(200, TimeUnit.MILLISECONDS).take(5)
    }.subscribe {
        Log.e(TAG, it.toString())
    }
}*/

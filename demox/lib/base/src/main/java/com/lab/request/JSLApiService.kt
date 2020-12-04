package com.lab.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lab.response.JSLKzz
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * jisilu api
 * Created by ak on 2020/10/13.
 */

private val BASE_URL = "https://www.jisilu.cn"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()


interface JSLApiService {

    @GET("/data/cbnew/cb_list/")
    suspend fun getCbList(@Query("___jsl=LST___t") t: String): JSLKzz
}

object JSLApi {
    val retrofitService: JSLApiService by lazy {
        retrofit.create(JSLApiService::class.java)
    }
}

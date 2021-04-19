package io.lab.request

import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface BaseNetworkApi {


    fun <T> getApi(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }


    fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder


    private val okHttpClient: OkHttpClient
        get() {
            return setHttpClientBuilder(
                RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
            ).build()
        }


}
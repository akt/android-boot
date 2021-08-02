package io.principle.api

import io.lab.request.BaseNetworkApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit


val apiService: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    NetworkApi.INSTANCE.getApi(ApiService::class.java, ApiService.BASE_URL)
}

class NetworkApi : BaseNetworkApi {

    companion object {
        val INSTANCE: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkApi()
        }
    }


    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        //TODO
        return OkHttpClient.Builder()
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        //TODO
        return Retrofit.Builder()
    }

}
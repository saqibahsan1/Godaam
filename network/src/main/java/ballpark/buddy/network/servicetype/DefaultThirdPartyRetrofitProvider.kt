package com.android.network.servicetype

import ballpark.buddy.network.GsonHelper
import ballpark.buddy.network.interceptor.ApiHeaderInterceptor
import ballpark.buddy.network.providers.OKHttpClientProvider
import ballpark.buddy.network.servicetype.ThirdPartyRetrofitProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class DefaultThirdPartyRetrofitProvider @Inject constructor(
    private val okHttpClientProvider: OKHttpClientProvider,
    private val apiHeaderInterceptor: ApiHeaderInterceptor
) : ThirdPartyRetrofitProvider {

    override fun get(): Retrofit {
        val okHttpBuilder = okHttpClientProvider.get()

        return Retrofit.Builder()
            .baseUrl(serviceType.baseURL)
            .client(
                okHttpBuilder
                    .addInterceptor(apiHeaderInterceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(GsonHelper.gsonIdentity))
            .build()
    }
}

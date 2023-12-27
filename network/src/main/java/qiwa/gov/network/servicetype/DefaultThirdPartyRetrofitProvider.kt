package com.android.network.servicetype

import qiwa.gov.network.GsonHelper
import qiwa.gov.network.interceptor.ApiHeaderInterceptor
import qiwa.gov.network.providers.OKHttpClientProvider
import qiwa.gov.network.servicetype.ThirdPartyRetrofitProvider
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

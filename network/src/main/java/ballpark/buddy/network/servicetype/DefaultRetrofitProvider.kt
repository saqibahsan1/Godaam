package ballpark.buddy.network.servicetype

import ballpark.buddy.network.GsonHelper
import ballpark.buddy.network.interceptor.ApiHeaderInterceptor
import ballpark.buddy.network.providers.OKHttpClientProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class DefaultRetrofitProvider @Inject constructor(
    private val okHttpClientProvider: OKHttpClientProvider,
    private val apiHeaderInterceptor: ApiHeaderInterceptor
) : AppRetrofitProvider {

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

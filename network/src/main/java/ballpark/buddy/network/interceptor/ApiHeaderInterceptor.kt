package ballpark.buddy.network.interceptor

import ballpark.buddy.network.providers.DefaultApiHeadersProvider.Companion.AUTHORIZATION
import ballpark.buddy.network.providers.DefaultApiHeadersProvider.Companion.DEVICE_ID
import ballpark.buddy.network.providers.ApiHeadersProvider
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

interface ApiHeaderInterceptor : Interceptor

class DefaultApiHeaderInterceptor @Inject constructor(
    private val apiHeadersProvider: ApiHeadersProvider,
) : ApiHeaderInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().apply {
                apiHeadersProvider.get().forEach { entry ->
                    if (entry.key != DEVICE_ID || !chain.call().request().url.toString().contains("payfort-token")) {
                        Timber.d("${entry.key} -- ${entry.value}")
                        addHeader(entry.key, entry.value)
                    }
                }
                if (removeToken(chain))
                    removeHeader(AUTHORIZATION)

            }.build()
        )
    }

    private fun removeToken(chain: Interceptor.Chain) =
        chain.call().request().url.toString().contains("refresh-token")
}

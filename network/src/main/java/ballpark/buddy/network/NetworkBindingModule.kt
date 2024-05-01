package ballpark.buddy.network

import com.android.network.servicetype.DefaultThirdPartyRetrofitProvider
import ballpark.buddy.network.providers.AppLocaleProvider
import ballpark.buddy.network.providers.DefaultAppLocaleProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ballpark.buddy.network.interceptor.ApiHeaderInterceptor
import ballpark.buddy.network.interceptor.CustomHttpLoggingInterceptor
import ballpark.buddy.network.interceptor.DefaultApiHeaderInterceptor
import ballpark.buddy.network.interceptor.HttpLoggingInterceptor
import ballpark.buddy.network.providers.ApiHeadersProvider
import ballpark.buddy.network.providers.AuthorizationTokenProvider
import ballpark.buddy.network.providers.DefaultApiHeadersProvider
import ballpark.buddy.network.providers.DefaultAuthorizationTokenProvider
import ballpark.buddy.network.providers.DefaultOKHttpClientProvider
import ballpark.buddy.network.providers.OKHttpClientProvider
import ballpark.buddy.network.servicetype.AppRetrofitProvider
import ballpark.buddy.network.servicetype.DefaultRetrofitProvider
import ballpark.buddy.network.servicetype.ThirdPartyRetrofitProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingModule {

    @Binds
    fun bindOKHttpClientProvider(default: DefaultOKHttpClientProvider): OKHttpClientProvider

    @Binds
    fun bindHttpLoggingInterceptor(default: CustomHttpLoggingInterceptor): HttpLoggingInterceptor

    @Binds
    fun bindApiHeaderInterceptor(default: DefaultApiHeaderInterceptor): ApiHeaderInterceptor

    @Binds
    fun bindApiHeadersProvider(default: DefaultApiHeadersProvider): ApiHeadersProvider

    @Binds
    fun bindAppLocaleProvider(default: DefaultAppLocaleProvider): AppLocaleProvider

    @Binds
    fun bindRetrofitProvider(default: DefaultRetrofitProvider): AppRetrofitProvider
    @Binds
    fun bindThirdPartyRetrofitProvider(default: DefaultThirdPartyRetrofitProvider): ThirdPartyRetrofitProvider
    @Binds
    @Singleton
    fun bindAuthorizationTokenProvider(default: DefaultAuthorizationTokenProvider): AuthorizationTokenProvider

    @Binds
    @Singleton
    fun bindNetworkPreferencesManager(default: DefaultNetworkPreferencesManager): NetworkPreferencesManager

    @Binds
    fun bindNetworkUtils(default: DefaultNetworkUtils): NetworkUtils

}

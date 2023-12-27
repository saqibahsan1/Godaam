package qiwa.gov.network

import com.android.network.servicetype.DefaultThirdPartyRetrofitProvider
import qiwa.gov.network.providers.AppLocaleProvider
import qiwa.gov.network.providers.DefaultAppLocaleProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import qiwa.gov.network.interceptor.ApiHeaderInterceptor
import qiwa.gov.network.interceptor.CustomHttpLoggingInterceptor
import qiwa.gov.network.interceptor.DefaultApiHeaderInterceptor
import qiwa.gov.network.interceptor.HttpLoggingInterceptor
import qiwa.gov.network.providers.ApiHeadersProvider
import qiwa.gov.network.providers.AuthorizationTokenProvider
import qiwa.gov.network.providers.DefaultApiHeadersProvider
import qiwa.gov.network.providers.DefaultAuthorizationTokenProvider
import qiwa.gov.network.providers.DefaultOKHttpClientProvider
import qiwa.gov.network.providers.OKHttpClientProvider
import qiwa.gov.network.servicetype.AppRetrofitProvider
import qiwa.gov.network.servicetype.DefaultRetrofitProvider
import qiwa.gov.network.servicetype.ThirdPartyRetrofitProvider
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

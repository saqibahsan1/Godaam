package qiwa.gov.sa.base.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import qiwa.gov.sa.cache.DefaultSharedPreferencesManager
import qiwa.gov.sa.cache.SharedPreferencesManager
import qiwa.gov.sa.dialog.DefaultDialogProvider
import qiwa.gov.sa.dialog.DefaultNoInternetDialogProvider
import qiwa.gov.sa.dialog.DialogProvider
import qiwa.gov.sa.dialog.NoInternetDialogProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ManagersBindings {

    @Binds
    fun bindDialogProvider(default: DefaultDialogProvider): DialogProvider

    @Binds
    @Singleton
    fun bindSharedPreferencesManager(default: DefaultSharedPreferencesManager): SharedPreferencesManager

}

@Module
@InstallIn(SingletonComponent::class)
interface ProvidersBindings {

//    @Binds
//    fun bindErrorDialogProvider(default: DefaultErrorDialogProvider): ErrorDialogProvider

//    @Binds
//    fun bindSuccessDialogProvider(default: DefaultSuccessDialogProvider): SuccessDialogProvider

    @Binds
    fun bindNoInternetDialogProvider(default: DefaultNoInternetDialogProvider): NoInternetDialogProvider

//    @Binds
//    fun bindInfoDialogProvider(default: DefaultGeneralDialogProvider): GeneralDialogProvider

}

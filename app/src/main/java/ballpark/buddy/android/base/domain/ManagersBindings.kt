package ballpark.buddy.android.base.domain

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ballpark.buddy.android.cache.DefaultSharedPreferencesManager
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.dialog.DefaultDialogProvider
import ballpark.buddy.android.dialog.DefaultErrorDialogProvider
import ballpark.buddy.android.dialog.DefaultGeneralDialogProvider
import ballpark.buddy.android.dialog.DefaultNoInternetDialogProvider
import ballpark.buddy.android.dialog.DialogProvider
import ballpark.buddy.android.dialog.ErrorDialogProvider
import ballpark.buddy.android.dialog.GeneralDialogProvider
import ballpark.buddy.android.dialog.NoInternetDialogProvider
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

    @Binds
    fun bindErrorDialogProvider(default: DefaultErrorDialogProvider): ErrorDialogProvider

//    @Binds
//    fun bindSuccessDialogProvider(default: DefaultSuccessDialogProvider): SuccessDialogProvider

    @Binds
    fun bindInfoDialogProvider(default: DefaultGeneralDialogProvider): GeneralDialogProvider

    @Binds
    fun bindNoInternetDialogProvider(default: DefaultNoInternetDialogProvider): NoInternetDialogProvider


}

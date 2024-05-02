package ballpark.buddy.android.ui.splash.domain

import android.graphics.drawable.Drawable
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.click_callback.NoInternetClickHandler
import ballpark.buddy.android.dialog.NoInternetDialogUiData
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.network.NetworkUtils
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.splash.presentation.SplashFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
    private val networkUtils: NetworkUtils,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : BaseViewModel() {


    fun onInit() {
        setSplashLoaderStopped(true)
        navigateToOnBoarding()
    }

    private fun getNoInternetDialogUiData(): NoInternetDialogUiData =
        NoInternetDialogUiData(
            buttonText = stringsResourceManager.getString(R.string.refresh),
            callback = object : NoInternetClickHandler {

                override fun canDismiss(): Boolean = networkUtils.hasInternet()

                override fun onClick() {
                    if (networkUtils.hasInternet()) {
                        onInit()
                    } else {
                        setUiMessage(UIMessage.ToastMessage(stringsResourceManager.getString(R.string.no_internet_connection)))
                    }
                }
            }
        )

    override fun getHeaderConfig(
        background: Drawable?,
        title: String,
        rightButtonType: HeaderRightButtonType,
        showBackButton: Boolean
    ): HeaderConfig {
        return super.getHeaderConfig(background, title, rightButtonType, showBackButton = false)
    }

    private fun navigateToOnBoarding() {
        if (sharedPreferencesManager.isOnBoardingVisited()) {
            if (sharedPreferencesManager.getAuthToken().isEmpty())
                navigateWithDelay(SplashFragmentDirections.actionSplashToLogin())
            else
                navigateWithDelay(SplashFragmentDirections.actionSplashToHome())
        } else {
            navigateWithDelay(SplashFragmentDirections.actionSplashToOnBoarding())
        }
    }

    private fun showNoInternetDialog() {
        setUiMessage(UIMessage.DialogMessage(DialogMessageType.NoInternet(getNoInternetDialogUiData())))
    }
}
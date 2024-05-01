package ballpark.buddy.android.base.domain

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ballpark.buddy.android.base.data.ActivityResultContractData
import ballpark.buddy.android.base.data.AppError
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.data.ViewState
import ballpark.buddy.android.base.data.unZipViewState
import ballpark.buddy.android.click_callback.ClickCallback
import ballpark.buddy.android.header.BackButtonConfig
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.header.TitleConfig
import ballpark.buddy.android.navigation.FragmentNavigator
import ballpark.buddy.android.navigation.NavigationLiveData
import ballpark.buddy.android.utils.Constants
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CustomScreenHeader {

    val navigationLiveData: LiveData<FragmentNavigator>
        get() = _navigationLiveData
    private val _navigationLiveData: NavigationLiveData by lazy {
        NavigationLiveData()
    }

    val isSplash: LiveData<Boolean>
        get() = _isSplash
    private val _isSplash: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val uiMessageLiveData: LiveData<UIMessage>
        get() = _uiMessageLiveData
    private val _uiMessageLiveData: SingleLiveEvent<UIMessage> by lazy {
        SingleLiveEvent()
    }

    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData
    private val _loadingLiveData: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent()
    }

    protected fun navigate(navDirections: NavDirections) {
        _navigationLiveData.value = FragmentNavigator.Push(navDirections)
    }

    protected fun navigate(intent: Intent) {
        _navigationLiveData.value = FragmentNavigator.ViaIntent(intent)
    }

    protected fun <I> navigateFromActivityContract(activityResultContractData: ActivityResultContractData.Launch<I>) {
        _navigationLiveData.value =
            FragmentNavigator.LaunchActivityResultContract(activityResultContractData)
    }

    fun navigateBack() {
        _navigationLiveData.value = FragmentNavigator.Pop
    }

    protected fun finishActivity() {
        _navigationLiveData.value = FragmentNavigator.Finish
    }

    private fun navigateToBottomNavItem(@IdRes idRes: Int) {
        _navigationLiveData.value = FragmentNavigator.BottomNavItem(idRes)
    }

    protected open fun launch(
        dispatcher: CoroutineContext = Dispatchers.Main,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            block()
        }
    }

    override fun getHeaderConfig(
        background: Drawable?,
        title: String,
        rightButtonType: HeaderRightButtonType,
        showBackButton: Boolean
    ): HeaderConfig {
        return HeaderConfig.DEFAULT.copy(
            backButtonConfig = getBackButtonConfig(showBackButton),
            titleConfig = getTitleConfig(title),
            headerRightButtonConfig = getHeaderRightButtonConfig(rightButtonType),
            itemBackground = background,
        )
    }

    protected open fun getBackButtonConfig(showBackButton: Boolean): BackButtonConfig =
        BackButtonConfig(
            canShowBackButton = showBackButton,
            clickCallback = object : ClickCallback<Unit> {
                override fun onClick(type: Unit) {
                    navigateBack()
                }
            }
        )

    private fun  getTitleConfig(title: String): TitleConfig = TitleConfig.DEFAULT.copy(title = title)

    private fun getHeaderRightButtonConfig(headerRightButtonType: HeaderRightButtonType): HeaderRightButtonConfig =
        HeaderRightButtonConfig(
            clickCallback = object : ClickCallback<HeaderRightButtonType> {
                override fun onClick(type: HeaderRightButtonType) {
                    when (type) {
                        is HeaderRightButtonType.Home -> {}/*navigate(LoginFragmentDirections.globalActionNotificationsListing())*/
                        HeaderRightButtonType.Home -> {}/*navigate(LoginFragmentDirections.globalActionPerformerHome())*/
                        else -> {
                            //
                        }
                    }
                }
            },
            headerRightButtonType = headerRightButtonType,
        )


    protected fun navigateWithDelay(navDirections: NavDirections, delay: Long = Constants.SCREEN_TRANSITIONS_DELAY) {
        launch {
            delay(delay)
            navigate(navDirections)
        }
    }

    protected fun navigateWithDelay(@IdRes bottomNavId: Int) {
        launch {
            delay(Constants.SCREEN_TRANSITIONS_DELAY)
            navigateToBottomNavItem(bottomNavId)
        }
    }

    protected fun setLoading(loading: Boolean) {
        _loadingLiveData.value = loading
    }

    protected fun setSplashLoaderStopped(loading: Boolean) {
        _isSplash.value = loading
    }

    protected fun setUiMessage(uiMessage: UIMessage) {
        _uiMessageLiveData.value = uiMessage
    }

    protected fun <T> unZipViewState(flow: Flow<ViewState<T>>, error: UIMessage.() -> Unit = {}, success: T.() -> Unit) {
        launch {
            flow.unZipViewState(
                loading = {
                    if (isSplash.value!!){
                        setLoading(false)
                    }else{
                        setLoading(this)
                    }
                },
                error = {
                    error.invoke(this)
                    setLoading(false)
                    setUiMessage(this)
                },
                success = {
                    setLoading(false)
                    success(this)
                }
            )
        }
    }

    protected fun onAppError(appError: AppError) {
        when (appError) {
            AppError.AccountError.UserNotFound,
            AppError.AccountError.UnAuthorizedUser -> {
                /*navigate(SplashFragmentDirections.login())*/
            }
            else -> return
        }
    }

//    protected fun showGeneralDialogUiData(generalDialogUiData: GeneralDialogUiData) {
//        setUiMessage(
//            UIMessage.DialogMessage(
//                DialogMessageType.GeneralDialog(generalDialogUiData)
//            )
//        )
//    }
}

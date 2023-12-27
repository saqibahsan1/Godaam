package qiwa.gov.sa.host

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import qiwa.gov.sa.base.data.DialogMessageType
import qiwa.gov.sa.base.data.FragmentConfig
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.domain.SingleLiveEvent
import qiwa.gov.sa.cache.SharedPreferencesManager
import qiwa.gov.sa.click_callback.ClickCallback
import qiwa.gov.sa.dialog.DialogProvider
import qiwa.gov.sa.dialog.NoInternetDialogUiData
import qiwa.gov.sa.header.HeaderConfig
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HostViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val dialogProvider: DialogProvider,
) : BaseViewModel() {

    val appHeaderLiveData: LiveData<HeaderConfig>
        get() = _appHeaderLiveData
    private val _appHeaderLiveData: MutableLiveData<HeaderConfig> by lazy {
        MutableLiveData()
    }
    val fragNavContainerTopMargin: LiveData<Int>
        get() = _fragNavContainerTopMargin
    private val _fragNavContainerTopMargin: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    val isPaymentMade: LiveData<Boolean>
        get() = _isPaymentMade
    private val _isPaymentMade: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val isPolicyUpdated: LiveData<Boolean>
        get() = _isPolicyUpdated
    val _isPolicyUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val bottomNavMenuItemId: LiveData<Int>
        get() = _bottomNavMenuItemId
    private val _bottomNavMenuItemId: SingleLiveEvent<Int> by lazy {
        SingleLiveEvent()
    }

    val updateBottomNavigation: LiveData<Int>
        get() = _updateBottomNavigation
    private val _updateBottomNavigation: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }

    val showBottomNavigation: LiveData<Boolean>
        get() = _showBottomNavigation
    private val _showBottomNavigation: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent()
    }
    val restartActivityLiveData: LiveData<Unit>
        get() = _restartActivityLiveData
    private val _restartActivityLiveData: SingleLiveEvent<Unit> by lazy {
        SingleLiveEvent()
    }
    private fun postAppHeaderUiData(headerConfig: HeaderConfig) {
        _appHeaderLiveData.value = headerConfig
    }

    fun postFragmentConfig(fragmentConfig: FragmentConfig) {
        postShowBottomNavigation(fragmentConfig.showFooter)
        postAppHeaderUiData(fragmentConfig.headerConfig)
        postFragNavContainerTopMargin(fragmentConfig.fragNavContainerTopMargin)
    }

    private fun postFragNavContainerTopMargin(height: Int) {
        _fragNavContainerTopMargin.value = height
    }

    private fun postShowBottomNavigation(show: Boolean) {
        _showBottomNavigation.value = show
    }

    fun postBottomNavMenuItemId(@IdRes menuResId: Int) {
        _bottomNavMenuItemId.value = menuResId
    }

    fun setLoadingFromHost(loading: Boolean) {
        setLoading(loading)
    }

    private fun showErrorDialogMessage(
        context: Context,
        message: String,
        description: String?,
        clickCallback: ClickCallback<Any>
    ) {
        dialogProvider.showErrorDialog(context, message, description, clickCallback, false)
    }

    fun showDialog(
        context: Context,
        dialogMessageType: DialogMessageType,
        clickCallback: ClickCallback<Any>
    ) {
        when (dialogMessageType) {
            is DialogMessageType.Error ->
                showErrorDialogMessage(
                    context,
                    dialogMessageType.message,
                    dialogMessageType.description,
                    clickCallback
                )


            is DialogMessageType.NoInternet ->
                showNoInternetDialog(context, dialogMessageType.noInternetDialogUiData)

        }
    }

    fun updateBottomNavigation(@MenuRes menu: Int) {
        _updateBottomNavigation.value = menu
    }

    private fun showNoInternetDialog(
        context: Context,
        noInternetDialogUiData: NoInternetDialogUiData
    ) {
        dialogProvider.showNoInternetDialog(context, noInternetDialogUiData)
    }
}

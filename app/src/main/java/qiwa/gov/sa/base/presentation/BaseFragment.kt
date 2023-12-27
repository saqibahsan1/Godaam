package qiwa.gov.sa.base.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import qiwa.gov.network.ApiErrors
import qiwa.gov.sa.base.data.ActivityResultContractData
import qiwa.gov.sa.base.data.DialogMessageType
import qiwa.gov.sa.base.data.FragmentConfig
import qiwa.gov.sa.base.data.UIMessage
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.click_callback.ClickCallback
import qiwa.gov.sa.extentions.getAppHeaderHeight
import qiwa.gov.sa.extentions.navigate
import qiwa.gov.sa.extentions.navigateBack
import qiwa.gov.sa.extentions.observeLiveData
import qiwa.gov.sa.extentions.showToast
import qiwa.gov.sa.host.HostViewModel
import qiwa.gov.sa.navigation.FragmentNavigator


abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    protected abstract fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    protected abstract val viewModel: BaseViewModel
    protected lateinit var binding: VB

    protected val hostViewModel: HostViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getBindView(inflater, container).apply {
            binding = this
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, viewModel)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postHeaderAndFooterConfig()
        observeNavigationLiveData()
        observeLoadingAndUiMessageLiveData()
    }

    private fun observeLoadingAndUiMessageLiveData() {
        observeLiveData(viewModel.uiMessageLiveData, ::handleUiMessage)
        observeLiveData(viewModel.loadingLiveData) { canShow ->
            hostViewModel.setLoadingFromHost(canShow)
        }
    }

    protected fun handleUiMessage(uiMessage: UIMessage) {
        when (uiMessage) {
            is UIMessage.ToastMessage -> showToast(uiMessage.message)
            is UIMessage.DialogMessage -> handleDialogMessageType(
                uiMessage.dialogMessageType,
                uiMessage.clickCallback
            )
        }
    }

    private fun handleDialogMessageType(
        dialogMessageType: DialogMessageType,
        clickCallback: ClickCallback<Any>
    ) {
        when (dialogMessageType) {
            is DialogMessageType.Error -> {
                if (dialogMessageType.errorCode == ApiErrors.UN_AUTH_ERROR_CODE) {
//                    hostViewModel.logout()
                    return
                }
                hostViewModel.showDialog(
                    requireContext(),
                    dialogMessageType,
                    if (dialogMessageType.errorCode == ApiErrors.UN_AUTHORIZED_USER_CODE) object :
                        ClickCallback<Any> {
                        override fun onClick(type: Any) {
                        }
                    } else clickCallback
                )
            }

            else -> hostViewModel.showDialog(requireContext(), dialogMessageType, clickCallback)
        }
    }

    fun hasOpenedDialogs(activity: FragmentActivity): Boolean {
        val fragments: List<Fragment> = activity.supportFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is DialogFragment) {
                return true
            }
        }
        return false
    }

    private fun postHeaderAndFooterConfig() {
        hostViewModel.postFragmentConfig(getFragmentConfig())
    }

    protected open fun getFragmentConfig(): FragmentConfig =
        FragmentConfig.DEFAULT.copy(
            navigationLiveData = viewModel.navigationLiveData,
            headerConfig = viewModel.getHeaderConfig(),
            showFooter = showFooter(),
            fragNavContainerTopMargin = getFragNavContainerTopMargin()
        )

    private fun observeNavigationLiveData() {
        observeLiveData(getFragmentConfig().navigationLiveData) { navigator ->
            when (navigator) {
                is FragmentNavigator.Push -> navigate(navigator.navDirections)

                is FragmentNavigator.BottomNavItem ->
                    hostViewModel.postBottomNavMenuItemId(navigator.menuItemId)

                is FragmentNavigator.LaunchActivityResultContract<*> ->
                    launchActivityResultContract(navigator.launchResult)

                is FragmentNavigator.ViaIntent -> startActivity(navigator.intent)

                FragmentNavigator.Finish -> requireActivity().finish()

                FragmentNavigator.Pop -> navigateBack()
            }
        }
    }

    private fun <I> launchActivityResultContract(
        data: ActivityResultContractData.Launch<I>
    ) {
        data.activityResultLauncher.launch(data.input)
    }

    protected open fun showFooter(): Boolean = true

    protected open fun getFragNavContainerTopMargin(): Int = getAppHeaderHeight()
}

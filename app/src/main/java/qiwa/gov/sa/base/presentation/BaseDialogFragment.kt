package qiwa.gov.sa.base.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import qiwa.gov.sa.R
import qiwa.gov.sa.base.data.ActivityResultContractData
import qiwa.gov.sa.base.data.DismissListener
import qiwa.gov.sa.base.data.UIMessage
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.domain.DialogDismissalHandler
import qiwa.gov.sa.extentions.navigate
import qiwa.gov.sa.extentions.navigateBack
import qiwa.gov.sa.extentions.observeLiveData
import qiwa.gov.sa.extentions.setWidthPercent
import qiwa.gov.sa.extentions.showToast
import qiwa.gov.sa.host.HostViewModel
import qiwa.gov.sa.navigation.FragmentNavigator

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment() {

    protected open val viewModel: BaseViewModel? = null
    abstract val dialogDismissHandler: DialogDismissalHandler

    protected val hostViewModel: HostViewModel by activityViewModels()

    protected open val dialogStyle: Int = R.style.DialogTheme

    protected abstract fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, dialogStyle)
    }

    protected fun attachDismissListener() {
        binding.run {
            setVariable(
                BR.dismissListener,
                object : DismissListener {
                    override fun onDismiss(value: Any?) {
                        dialogDismissHandler.onDialogDismissed(value)
                        dismiss()
                    }
                }
            )
        }
    }

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
        viewModel?.let { viewModel ->
            observeLiveData(viewModel.navigationLiveData) { navigator ->
                when (navigator) {
                    is FragmentNavigator.Push -> navigate(navigator.navDirections)
                    is FragmentNavigator.BottomNavItem ->
                        hostViewModel.postBottomNavMenuItemId(navigator.menuItemId)
                    FragmentNavigator.Finish -> requireActivity().finish()
                    FragmentNavigator.Pop -> navigateBack()
                    is FragmentNavigator.LaunchActivityResultContract<*> ->
                        launchActivityResultContract(navigator.launchResult)
                    else -> {}
                }
            }

            observeLiveData(viewModel.uiMessageLiveData, ::handleUiMessage)
            observeLiveData(viewModel.loadingLiveData) { canShow ->
                hostViewModel.setLoadingFromHost(canShow)
            }
        }
    }

    private fun handleUiMessage(uiMessage: UIMessage) {
        when (uiMessage) {
            is UIMessage.ToastMessage -> showToast(uiMessage.message)
            is UIMessage.DialogMessage -> hostViewModel.showDialog(requireContext(), uiMessage.dialogMessageType, uiMessage.clickCallback)
        }
    }

    override fun onResume() {
        super.onResume()
        setWidthPercent(90)
    }

    private fun <I> launchActivityResultContract(
        data: ActivityResultContractData.Launch<I>
    ) {
        data.activityResultLauncher.launch(data.input)
    }
}
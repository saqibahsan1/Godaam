package ballpark.buddy.android.ui.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.CreateAccountFragmentBinding
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.extentions.getDefaultAppHeaderHeight
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.extentions.observeLiveData
import ballpark.buddy.android.ui.auth.domain.CreateAccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : BaseFragment<CreateAccountFragmentBinding>() {

    private val createAccountViewModel: CreateAccountViewModel by viewModels()

    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): CreateAccountFragmentBinding {
        return CreateAccountFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData(createAccountViewModel.accountCreatedSuccess) {
            if (it.first.inverse)
                createAccountViewModel.showDialogUiMessage(it.second.default)
            else
                createAccountViewModel.showDialogUiSuccessMessage()

        }
    }

    override fun showFooter(): Boolean = false

    override fun getFragNavContainerTopMargin(): Int = getDefaultAppHeaderHeight()

    override val viewModel: BaseViewModel
        get() = createAccountViewModel

}
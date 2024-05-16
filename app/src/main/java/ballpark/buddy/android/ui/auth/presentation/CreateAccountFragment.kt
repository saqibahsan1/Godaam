package ballpark.buddy.android.ui.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.CreateAccountFragmentBinding
import ballpark.buddy.android.extentions.clickToAction
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
        createAccountViewModel.accountTypeSearchableSpinner(
            requireActivity(),
            binding.accountTypeEt
        )
        createAccountViewModel.leagueSearchableSpinner(requireActivity(), binding.leagueEt)

        binding.leagueEt.setOnClickListener { createAccountViewModel.onClickLeagueDropDown() }
        binding.leagueEt.editText?.setOnClickListener { createAccountViewModel.onClickLeagueDropDown() }

        binding.accountTypeEt.setOnClickListener { createAccountViewModel.onClickOnAccountTypeDropDown() }
        binding.accountTypeEt.editText?.setOnClickListener { createAccountViewModel.onClickOnAccountTypeDropDown() }


        binding.checkBoxCash.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkBoxZelle.isChecked = false
                binding.checkBoxVenmo.isChecked = false
                binding.checkBoxCashApp.isChecked = false
            }
        }
        binding.checkBoxZelle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkBoxCash.isChecked = false
                binding.checkBoxVenmo.isChecked = false
                binding.checkBoxCashApp.isChecked = false
            }
        }
        binding.checkBoxVenmo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkBoxCash.isChecked = false
                binding.checkBoxZelle.isChecked = false
                binding.checkBoxCashApp.isChecked = false
            }
        }
        binding.checkBoxCashApp.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkBoxCash.isChecked = false
                binding.checkBoxZelle.isChecked = false
                binding.checkBoxVenmo.isChecked = false
            }
        }
        observeLiveData(createAccountViewModel.accountCreatedSuccess) {
            if (it.first.inverse)
                createAccountViewModel.showDialogUiMessage(it.second.default)

        }
    }

    override fun showFooter(): Boolean = false

    override fun getFragNavContainerTopMargin(): Int = getDefaultAppHeaderHeight()

    override val viewModel: BaseViewModel
        get() = createAccountViewModel

}
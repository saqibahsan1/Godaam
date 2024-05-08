package ballpark.buddy.android.ui.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.CreateAccountFragmentBinding
import ballpark.buddy.android.extentions.getDefaultAppHeaderHeight
import ballpark.buddy.android.ui.auth.domain.CreateAccountViewModel
import com.google.firebase.FirebaseApp
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
        createAccountViewModel.accountTypeSearchableSpinner(requireActivity(),binding.accountTypeEt)
        createAccountViewModel.searchableSpinner(requireActivity(),binding.leagueEt)
        binding.checkBoxCash.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.checkBoxZelle.isChecked = false
                binding.checkBoxVenmo.isChecked = false
                binding.checkBoxCashApp.isChecked = false
            }
        }
        binding.checkBoxZelle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.checkBoxCash.isChecked = false
                binding.checkBoxVenmo.isChecked = false
                binding.checkBoxCashApp.isChecked = false
            }
        }
        binding.checkBoxVenmo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.checkBoxCash.isChecked = false
                binding.checkBoxZelle.isChecked = false
                binding.checkBoxCashApp.isChecked = false
            }
        }
        binding.checkBoxCashApp.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.checkBoxCash.isChecked = false
                binding.checkBoxZelle.isChecked = false
                binding.checkBoxVenmo.isChecked = false
            }
        }
    }

    override fun showFooter(): Boolean = false

    override fun getFragNavContainerTopMargin(): Int = getDefaultAppHeaderHeight()

    override val viewModel: BaseViewModel
        get() = createAccountViewModel

}
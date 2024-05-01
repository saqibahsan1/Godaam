package ballpark.buddy.android.ui.calculators.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.WorkPermitFragmentBinding
import ballpark.buddy.android.extentions.observeLiveData
import ballpark.buddy.android.ui.calculators.domain.WorkPermitViewModel

@AndroidEntryPoint
class WorkPermitFragment : BaseFragment<WorkPermitFragmentBinding>() {

    private val workPermitViewModel: WorkPermitViewModel by viewModels()
    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): WorkPermitFragmentBinding {
        return WorkPermitFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData(workPermitViewModel.displayResult) {
            binding.scrollViewWorkPermit.fullScroll(View.FOCUS_UP)
        }
    }

    override val viewModel: BaseViewModel
        get() = workPermitViewModel
}
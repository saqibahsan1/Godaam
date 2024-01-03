package qiwa.gov.sa.ui.calculators.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.presentation.BaseFragment
import qiwa.gov.sa.databinding.WorkPermitFragmentBinding
import qiwa.gov.sa.extentions.observeLiveData
import qiwa.gov.sa.ui.calculators.domain.WorkPermitViewModel

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
package qiwa.gov.sa.ui.calculators.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.presentation.BaseFragment
import qiwa.gov.sa.databinding.EndServiceCalculatorFragmentBinding
import qiwa.gov.sa.ui.calculators.domain.EndServiceCalculatorViewModel

@AndroidEntryPoint
class EndServiceCalculator : BaseFragment<EndServiceCalculatorFragmentBinding>(){
    private val endServiceCalculatorViewModel : EndServiceCalculatorViewModel by viewModels()
    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): EndServiceCalculatorFragmentBinding {
        return EndServiceCalculatorFragmentBinding.inflate(inflater,container,false)
    }

    override val viewModel: BaseViewModel
        get() = endServiceCalculatorViewModel
}
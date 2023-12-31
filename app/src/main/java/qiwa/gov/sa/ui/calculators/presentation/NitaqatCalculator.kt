package qiwa.gov.sa.ui.calculators.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.presentation.BaseFragment
import qiwa.gov.sa.databinding.NitaqatCalculatorFragmentBinding
import qiwa.gov.sa.ui.calculators.domain.NitaqatViewModel

@AndroidEntryPoint
class NitaqatCalculator : BaseFragment<NitaqatCalculatorFragmentBinding>(){

    private val nitaqatCalculator : NitaqatViewModel by viewModels()
    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NitaqatCalculatorFragmentBinding {
        return NitaqatCalculatorFragmentBinding.inflate(inflater,container,false)
    }

    override val viewModel: BaseViewModel
        get() = nitaqatCalculator
}
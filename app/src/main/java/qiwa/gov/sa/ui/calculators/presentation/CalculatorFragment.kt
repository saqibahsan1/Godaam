package qiwa.gov.sa.ui.calculators.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.presentation.BaseFragment
import qiwa.gov.sa.databinding.FragmentCalculatorBinding
import qiwa.gov.sa.ui.calculators.domain.CalculatorViewModel

@AndroidEntryPoint
class CalculatorFragment : BaseFragment<FragmentCalculatorBinding>() {

    private val calculatorViewModel : CalculatorViewModel by viewModels()
    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCalculatorBinding {
        return FragmentCalculatorBinding.inflate(inflater, container, false)
    }

    override val viewModel: BaseViewModel
        get() = calculatorViewModel

}
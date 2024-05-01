package ballpark.buddy.android.ui.calculators.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.FragmentCalculatorBinding
import ballpark.buddy.android.ui.calculators.domain.CalculatorViewModel

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
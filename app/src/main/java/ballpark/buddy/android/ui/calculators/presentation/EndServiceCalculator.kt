package ballpark.buddy.android.ui.calculators.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.EndServiceCalculatorFragmentBinding
import ballpark.buddy.android.ui.calculators.domain.EndServiceCalculatorViewModel

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
package ballpark.buddy.android.ui.calculators.presentation

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.fragment.app.viewModels
import com.android.wearecovered.resources.StringsResourceManager
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.R
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.NitaqatCalculatorFragmentBinding
import ballpark.buddy.android.extentions.observeLiveData
import ballpark.buddy.android.ui.calculators.domain.NitaqatViewModel
import javax.inject.Inject


@AndroidEntryPoint
class NitaqatCalculator : BaseFragment<NitaqatCalculatorFragmentBinding>() {

    private val nitaqatCalculatorViewModel: NitaqatViewModel by viewModels()

    @Inject
    lateinit var stringsResourceManager: StringsResourceManager
    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NitaqatCalculatorFragmentBinding {
        return NitaqatCalculatorFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spannableText()
        nitaqatCalculatorViewModel.readFromJson()
    }

    override val viewModel: BaseViewModel
        get() = nitaqatCalculatorViewModel

    private fun spannableText() {
        nitaqatCalculatorViewModel.searchableSpinner(requireActivity(),binding.dropDownSubEconomic)
//        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
//        (binding.dropDownSubEconomic.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items)
        observeLiveData(nitaqatCalculatorViewModel.displayResult) {
            binding.scrollView.fullScroll(View.FOCUS_UP)
        }
        val noteText = SpannableStringBuilder()
            .bold { append(stringsResourceManager.getString(R.string.note)) }
            .append(stringsResourceManager.getString(R.string.noteDesc))
        binding.noteText.text = noteText

        val ntaqatText = SpannableStringBuilder()
            .bold { append(stringsResourceManager.getString(R.string.nitaqat)) }
            .append(stringsResourceManager.getString(R.string.nitaqatDesc))
        binding.nitaqaText.text = ntaqatText

        val quotaText = SpannableStringBuilder()
            .bold { append(stringsResourceManager.getString(R.string.quotaText)) }
            .append(stringsResourceManager.getString(R.string.pendingVisas))
        binding.quotaText.text = quotaText
    }


}
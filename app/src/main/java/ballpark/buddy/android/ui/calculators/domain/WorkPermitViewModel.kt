package ballpark.buddy.android.ui.calculators.domain

import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.wearecovered.resources.StringsResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import ballpark.buddy.android.base.domain.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class WorkPermitViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager
) : BaseViewModel() {
    private var amount = 0
    val displayResult: LiveData<Boolean>
        get() = _displayResult
    private val _displayResult: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    fun calculateNitaqat(calculate: Boolean) {
        _displayResult.value = calculate
    }

    fun onAddAmountClick(field: AppCompatTextView) {
        amount = amount.inc()
        field.text = amount.toString()
    }

    fun onSubAmountClick(field: AppCompatTextView) {
        amount = field.text.toString().toInt()
        if (amount != 0)
            amount = amount.dec()
        field.text = amount.toString()
    }
}
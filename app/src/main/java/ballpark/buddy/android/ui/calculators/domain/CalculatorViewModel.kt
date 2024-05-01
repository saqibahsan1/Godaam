package ballpark.buddy.android.ui.calculators.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.ui.calculators.presentation.CalculatorFragmentDirections
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(): BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is a calculator Screen"
    }
    val text: LiveData<String> = _text

    fun navigateToNitaqat(){
        navigate(CalculatorFragmentDirections.navCalculatorsToNitaqat())
    }

    fun navigateToEndOfService(){
        navigate(CalculatorFragmentDirections.navCalculatorsToEndService())
    }
    fun navigateToWorkPermitFragment(){
        navigate(CalculatorFragmentDirections.navCalculatorsToWorkPermit())
    }
}
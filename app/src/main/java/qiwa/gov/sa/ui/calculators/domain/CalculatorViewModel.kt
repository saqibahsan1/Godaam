package qiwa.gov.sa.ui.calculators.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.ui.calculators.presentation.CalculatorFragment
import qiwa.gov.sa.ui.calculators.presentation.CalculatorFragmentDirections
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
}
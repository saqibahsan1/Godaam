package qiwa.gov.sa.ui.calculators.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.ui.calculators.presentation.NitaqatCalculatorDirections
import javax.inject.Inject

@HiltViewModel
class NitaqatViewModel @Inject constructor() : BaseViewModel()  {

    private val url ="https://www.qiwa.sa/en/business-owners/manage-establishment/what-nitaqat-and-how-it-calculated"
    fun navigateToWebView(){
        navigate(NitaqatCalculatorDirections.navNitaqatToWebView(url))
    }

    val displayResult: LiveData<Boolean>
        get() = _displayResult
    private val _displayResult: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    fun calculateNitaqat(calculate: Boolean){
        _displayResult.value = calculate
    }

}
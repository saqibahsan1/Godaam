package qiwa.gov.sa.ui.calculators.domain

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import qiwa.gov.sa.base.domain.BaseViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EndServiceCalculatorViewModel @Inject constructor() : BaseViewModel() {

    val displayResult: LiveData<Boolean>
        get() = _displayResult
    private val _displayResult: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val startDate: LiveData<String>
        get() = _startDate
    private val _startDate: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    val endDate: LiveData<String>
        get() = _endDate
    private val _endDate: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun calculateNitaqat(calculate: Boolean) {
        _displayResult.value = calculate
    }

    private val calendar = Calendar.getInstance()
    fun showDatePicker(context: Context, type: Int) {
        val datePickerDialog = DatePickerDialog(
            context, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                if (type == 0)
                    _startDate.value = formattedDate
                else
                    _endDate.value = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}
package ballpark.buddy.android.ui.calculators.domain

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ballpark.buddy.android.resources.StringsResourceManager
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.reflect.TypeToken
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ballpark.buddy.android.base.domain.BaseViewModel
import com.leo.searchablespinner.utils.data.NitaqatDropDownDataItem
import ballpark.buddy.android.R
import ballpark.buddy.android.extentions.parseArray
import ballpark.buddy.android.ui.calculators.presentation.NitaqatCalculatorDirections
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class NitaqatViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stringsResourceManager: StringsResourceManager
) : BaseViewModel() {

    private val nitaqatFile = "nitaqat.json"
    private val url =
        "https://www.qiwa.sa/en/business-owners/manage-establishment/what-nitaqat-and-how-it-calculated"

    fun navigateToWebView() {
        navigate(NitaqatCalculatorDirections.navNitaqatToWebView(url))
    }

    val displayResult: LiveData<Boolean>
        get() = _displayResult
    private val _displayResult: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    fun calculateNitaqat(calculate: Boolean) {
        _displayResult.value = calculate
    }

    fun readFromJson(): List<NitaqatDropDownDataItem> {
        val fileInString: String =
            context.assets.open(nitaqatFile).bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<NitaqatDropDownDataItem>>() {}.type
        return parseArray<List<NitaqatDropDownDataItem>>(json = fileInString, typeToken = type)
    }

    fun searchableSpinner(context: FragmentActivity, dropDownSubEconomic: TextInputLayout) {
        val searchableSpinner = SearchableSpinner(context)
        searchableSpinner.windowTitle = stringsResourceManager.getString(R.string.subEconomic)
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                Toast.makeText(
                    context,
                    "${searchableSpinner.selectedItem?.Economic_Activity}  ${searchableSpinner.selectedItem?.Economic_Activity_Id}",
                    Toast.LENGTH_SHORT
                ).show()
                dropDownSubEconomic.editText?.setText(selectedString)
            }
        }
//        searchableSpinner.setSpinnerListItems(readFromJson())
        dropDownSubEconomic.editText?.keyListener = null
        dropDownSubEconomic.editText?.setOnClickListener {
            searchableSpinner.show()
        }
    }
}
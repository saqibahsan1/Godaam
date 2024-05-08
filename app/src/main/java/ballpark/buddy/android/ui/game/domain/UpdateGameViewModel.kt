package ballpark.buddy.android.ui.game.domain

import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateHandle
import ballpark.buddy.android.R
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.resources.DrawableResourceManager
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.home.data.HomeUiData
import ballpark.buddy.android.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.leo.searchablespinner.utils.data.JobItems
import com.leo.searchablespinner.utils.data.LeagueItems
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UpdateGameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stringsResourceManager: StringsResourceManager,
    private val drawableResourceManager: DrawableResourceManager,
) : BaseViewModel() {

    val gameData: HomeUiData by lazy {
        savedStateHandle.get<HomeUiData>("HomeUiData")
            ?: throw IllegalAccessException("HomeUiData must not be null")
    }

    fun getDate(): String? {
        try {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val netDate = Date(gameData.postTime?.times(1000).default)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getTime(): String? {
        try {
            val sdf = SimpleDateFormat("hh:mm a", Locale.US)
            val netDate = Date(gameData.postTime?.times(1000).default)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private val db = FirebaseFirestore.getInstance()
    private fun getDropDownItems(): List<LeagueItems> {
        val items = mutableListOf<LeagueItems>()
        db.collection(Constants.LEAGUE_NAME_TABLE).get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val leagueItems = document.toObject(LeagueItems::class.java)
                    if (leagueItems != null) {
                        items.add(leagueItems)
                    }
                }
            }.addOnFailureListener { exception ->
                // Handle the error
                Timber.e(exception.message)
                // You can add error handling logic here
            }
        return items
    }

    private fun getDropDownJobItems(onComplete: (List<JobItems>) -> Unit): List<JobItems> {
        val items = mutableListOf<JobItems>()
        db.collection(Constants.JOB_DUTY_TABLE).get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val jobItemData = document.toObject(JobItems::class.java)
                    if (jobItemData != null) {
                        items.add(jobItemData)
                    }
                }
                onComplete(items)
            }.addOnFailureListener { exception ->
                // Handle the error
                Timber.e(exception.message)
                // You can add error handling logic here
            }
        return items
    }

    fun searchableSpinner(context: FragmentActivity, leagueItemDropDown: CustomEditTextField) {
        val searchableSpinner = SearchableSpinner(context)
        searchableSpinner.windowTitle = stringsResourceManager.getString(R.string.selectLeague)
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                leagueItemDropDown.editText.setText(selectedString)
            }
        }
        searchableSpinner.setSpinnerListItems(getDropDownItems())
        leagueItemDropDown.editText.keyListener = null
        leagueItemDropDown.editText.setOnClickListener {
            searchableSpinner.show()
        }
    }

    fun jobItemSpinner(context: FragmentActivity, jobItemDropDown: CustomEditTextField) {
        val searchableSpinner = SearchableSpinner(context)
        searchableSpinner.windowTitle = stringsResourceManager.getString(R.string.jobDuty)
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                jobItemDropDown.editText.setText(selectedString)
            }
        }
        getDropDownJobItems { items ->
            val jobItemList = items.map {
                LeagueItems(
                    ID = it.ID,
                    leagueName = it.jobDutyTitle
                )
            }
            searchableSpinner.setSpinnerListItems(jobItemList)
        }
        jobItemDropDown.editText.keyListener = null
        jobItemDropDown.editText.setOnClickListener {
            searchableSpinner.show()
        }
    }

    override fun getHeaderConfig(
        background: Drawable?,
        title: String,
        rightButtonType: HeaderRightButtonType,
        showBackButton: Boolean
    ): HeaderConfig {
        return super.getHeaderConfig(
            background = drawableResourceManager.getDrawable(R.drawable.common_square),
            title = stringsResourceManager.getString(R.string.game),
            rightButtonType = HeaderRightButtonType.None,
            showBackButton = true
        )
    }
}
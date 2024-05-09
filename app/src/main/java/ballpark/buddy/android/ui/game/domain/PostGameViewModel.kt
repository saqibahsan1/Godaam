package ballpark.buddy.android.ui.game.domain

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.click_callback.ClickCallback
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.EMPTY_STRING
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.resources.DrawableResourceManager
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.home.data.GameUiData
import ballpark.buddy.android.utils.Constants
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.leo.searchablespinner.utils.data.JobItems
import com.leo.searchablespinner.utils.data.LeagueItems
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PostGameViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
    private val drawableResourceManager: DrawableResourceManager,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : BaseViewModel() {
    fun onTapOfUpdateGame(
        leagueEt: CustomEditTextField,
        jobDutyTextEt: CustomEditTextField,
        teamEt: CustomEditTextField,
        nameEt: CustomEditTextField,
        fieldEt: CustomEditTextField,
        payEt: CustomEditTextField,
    ) {
        if (leagueEt.getFieldText().isEmpty()){
            leagueEt.setError("Please select the league", leagueEt)
            leagueEt.requestFocus()
            return
        }
        if (jobDutyTextEt.getFieldText().isEmpty()){
            jobDutyTextEt.setError("Please select the Job Duty", jobDutyTextEt)
            jobDutyTextEt.requestFocus()
            return
        }
        if (fieldEt.getFieldText().isEmpty()) {
            fieldEt.setError("Please enter your field", fieldEt)
            fieldEt.requestFocus()
            return
        }
        if (payEt.getFieldText().isEmpty()) {
            payEt.setError("Please enter the pay", payEt)
            payEt.requestFocus()
            return
        }
        if (teamEt.getFieldText().isEmpty()) {
            teamEt.setError("Please enter your team", teamEt)
            teamEt.requestFocus()
            return
        }
        if (nameEt.getFieldText().isEmpty()) {
            nameEt.setError("Please enter your name", nameEt)
            nameEt.requestFocus()
            return
        }
        val gameUiData = GameUiData(
            gameId = Constants.generateRandomId(),
            leagueName = leagueEt.getFieldText(),
            jobDuty = jobDutyTextEt.getFieldText(),
            team = teamEt.getFieldText(),
            postedByName = nameEt.getFieldText(),
            field = fieldEt.getFieldText(),
            payAmount = payEt.getFieldText(),
            postTime = Constants.getCurrentUnixTimestamp(),
            bookKeeper = EMPTY_STRING,
            isNewUpdates = false,
            parent = "${sharedPreferencesManager.getUserObject()?.firstName} ${sharedPreferencesManager.getUserObject()?.lastName}",
            postedBy = sharedPreferencesManager.getUserId()
        )
        updatePost(gameUiData)
        setLoading(true)
    }

    private fun mergeTimeStamp(): Long{
        if (dateTimestamp != 0L && timestampTime != 0L) {
            val dateTimeMills = dateTimestamp + timestampTime
            dateTimestamp = Date(dateTimeMills).time
        }else if (dateTimestamp == 0L){
            val dateTimeMills = Constants.getCurrentUnixTimestamp() + timestampTime
            dateTimestamp = Date(dateTimeMills).time
        }else if (timestampTime == 0L){
            val dateTimeMills = dateTimestamp + Calendar.getInstance().timeInMillis
            dateTimestamp = Date(dateTimeMills).time
        }
        return dateTimestamp
    }
    private fun updatePost(gameData: GameUiData) {
        val collectionRef = db.collection(Constants.GAME_TABLE_STAGE)
        collectionRef.document(gameData.gameId.default).set(gameData)
            .addOnCompleteListener { task ->
                setLoading(false)
                if (task.isSuccessful)
                    showSuccessDialogUiMessage()
                else
                    showDialogUiMessage(task.exception?.message.default)
            }
    }

    private fun showSuccessDialogUiMessage() {
        setUiMessage(
            UIMessage.DialogMessage(
                DialogMessageType.GeneralDialog(
                    GeneralDialogUiData(
                        title = stringsResourceManager.getString(R.string.success),
                        description = stringsResourceManager.getString(R.string.game_updated),
                        primaryButtonText = stringsResourceManager.getString(R.string.ok),
                        isCancelable = false,
                        primaryButtonListener = object : ClickCallback<Any> {
                            override fun onClick(type: Any) {
                                navigateBack()
                            }

                        }
                    )
                )
            )
        )
    }

    fun currentUserName() = "${sharedPreferencesManager.getUserObject()?.firstName} ${sharedPreferencesManager.getUserObject()?.lastName}"

    private fun showDialogUiMessage(error: String) {
        setUiMessage(
            UIMessage.DialogMessage(
                DialogMessageType.Error(
                    message = error,
                    isCancelable = false
                )
            )
        )
    }


    fun getDate(): String? {
        try {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val netDate = Date(Calendar.getInstance().timeInMillis)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getTime(): String? {
        try {
            val sdf = SimpleDateFormat("hh:mm a", Locale.US)
            val netDate = Date(Calendar.getInstance().timeInMillis)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private val calendar = Calendar.getInstance()
    private var dateTimestamp: Long = 0
    private var timestampTime: Long = 0
    fun showDatePicker(context: Context, tvSelectedDate: AppCompatTextView) {
        val datePickerDialog = DatePickerDialog(
            context, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                tvSelectedDate.text = formattedDate
                val timestamp = Timestamp(Date(selectedDate.timeInMillis))
                dateTimestamp = timestamp.seconds
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun showTimePicker(context: Context, tvSelectedTime: AppCompatTextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            tvSelectedTime.text = SimpleDateFormat("HH:mm a", Locale.US).format(cal.time)
            timestampTime = cal.time.time
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()
    }

    private val db = FirebaseFirestore.getInstance()
    private fun getDropDownItemsForLeague(): List<LeagueItems> {
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
                Timber.e(exception.message)
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
                Timber.e(exception.message)
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
        searchableSpinner.setSpinnerListItems(getDropDownItemsForLeague())
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
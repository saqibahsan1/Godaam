package ballpark.buddy.android.ui.auth.domain

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.EMPTY_STRING
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.extentions.navigate
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.resources.DrawableResourceManager
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.auth.data.User
import ballpark.buddy.android.ui.auth.presentation.CreateAccountFragmentDirections
import ballpark.buddy.android.utils.Constants
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.leo.searchablespinner.SearchableSpinner
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.leo.searchablespinner.utils.data.AccountItems
import com.leo.searchablespinner.utils.data.LeagueItems
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : BaseViewModel() {
    private lateinit var auth: FirebaseAuth
    val accountCreatedSuccess: LiveData<Pair<Boolean, String?>>
        get() = _accountCreatedSuccess
    private val _accountCreatedSuccess: MutableLiveData<Pair<Boolean, String?>> by lazy {
        MutableLiveData()
    }

    fun showDialogUiMessage(error: String) {
        setUiMessage(
            UIMessage.DialogMessage(
                DialogMessageType.Error(
                        message = error,
                        isCancelable = false
                    )
                )
            )
    }

    override fun getHeaderConfig(
        background: Drawable?,
        title: String,
        rightButtonType: HeaderRightButtonType,
        showBackButton: Boolean
    ): HeaderConfig {
        return super.getHeaderConfig(
            background,
            title,
            rightButtonType = HeaderRightButtonType.None,
            showBackButton = false
        )
    }
    fun navigateToLogin(){}

    private var checkBoxValue : String = EMPTY_STRING
    private val db = FirebaseFirestore.getInstance()
    fun onTapOfSignupButton(email: CustomEditTextField, password: CustomEditTextField, confirmPassword: CustomEditTextField,
                            firsNameEditText: CustomEditTextField, lastNameEditText: CustomEditTextField
                            ,zipCodeEditText: CustomEditTextField ,accountTypeEt: CustomEditTextField ,leagueEt: CustomEditTextField
                            ,checkBoxCash: MaterialCheckBox,checkBoxVenmo: MaterialCheckBox,checkBoxCashApp: MaterialCheckBox,checkBoxZelle: MaterialCheckBox) {
        if (email.isValid().inverse) {
            email.setError("Please enter you email address", email)
            email.requestFocus()
            return
        }
        if (firsNameEditText.isValid().inverse){
            firsNameEditText.setError("Please enter your first name", firsNameEditText)
            firsNameEditText.requestFocus()
            return
        }
        if (lastNameEditText.isValid().inverse){
            lastNameEditText.setError("Please enter your last name", lastNameEditText)
            lastNameEditText.requestFocus()
            return
        }
        if (password.isValid().inverse) {
            password.setError("Please enter your password", password)
            password.requestFocus()
            return
        }
        if (confirmPassword.isValid().inverse) {
            password.setError("Please enter your password", confirmPassword)
            confirmPassword.requestFocus()
            return
        }
        if (password.getFieldText() != confirmPassword.getFieldText()){
            confirmPassword.setError("password did not matched", confirmPassword)
            confirmPassword.requestFocus()
            return
        }
        if (zipCodeEditText.isValid().inverse){
            zipCodeEditText.setError("Please enter your zip code", zipCodeEditText)
            zipCodeEditText.requestFocus()
            return
        }
        if (accountTypeEt.isValid().inverse){
            accountTypeEt.setError("Please select account type", accountTypeEt)
            accountTypeEt.requestFocus()
            return
        }
        if (leagueEt.isValid().inverse){
            leagueEt.setError("Please select the league", leagueEt)
            leagueEt.requestFocus()
            return
        }
        if (checkBoxCash.isChecked){
            checkBoxValue = checkBoxCash.text.toString()
        }
        if (checkBoxVenmo.isChecked){
            checkBoxValue = checkBoxVenmo.text.toString()
        }
        if (checkBoxCashApp.isChecked){
            checkBoxValue = checkBoxCashApp.text.toString()
        }
        if (checkBoxZelle.isChecked){
            checkBoxValue = checkBoxZelle.text.toString()
        }
        if (checkBoxValue.isEmpty()) {
            showDialogUiMessage(stringsResourceManager.getString(R.string.paymentError))
            return
        }
        setLoading(true)
        createUser(
            firstName = firsNameEditText.getFieldText(),
            lastName = lastNameEditText.getFieldText(),
            email = email.getFieldText(),
            password = password.getFieldText(),
            league = leagueEt.getFieldText(),
            accountType = accountTypeEt.getFieldText(),
            zipCode = zipCodeEditText.getFieldText()
        )
    }
    private fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        league: String?,
        accountType: String?,
        zipCode: String?,
    ) {
        val user = User(
            Constants.getCurrentUnixTimestamp(), firstName = firstName, lastName = lastName, email = email, fcmToken = EMPTY_STRING,accountType = accountType,
            league = league, userId = EMPTY_STRING, paymentType = checkBoxValue, zipCode = zipCode
        )
        registerUser(email, password, user){ success, errorMessage ->
            setLoading(false)
            val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            sharedPreferencesManager.setUserId(userId)
            getUserData(userId)
            _accountCreatedSuccess.value = Pair(success, errorMessage)
        }
    }

    private fun getUserData(userId: String) {
        db.collection(Constants.USER_TABLE_STAGE).document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        val userData = document.toObject(User::class.java)
                        sharedPreferencesManager.setUserObject(userData)
                        navigate(CreateAccountFragmentDirections.actionCreateAccountToHome())
                    }else{
                        showDialogUiMessage("User not found")
                    }
                }
                setLoading(false)
            }
    }

    private fun registerUser(
        email: String,
        password: String,
        user: User,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.uid?.let { userId ->
                        user.userId = userId
                        db.collection(Constants.USER_TABLE_STAGE).document(userId)
                            .set(user)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    onComplete(true, null)
                                } else {
                                    onComplete(false, firestoreTask.exception?.message)
                                }
                            }
                    }
                } else {
                    val exception = task.exception as? FirebaseAuthException
                    onComplete(false, exception?.message ?: "Registration failed.")
                }
            }
    }
    private fun getDropDownItems():List<LeagueItems>{
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
    fun accountTypeSearchableSpinner(context: FragmentActivity, accountTypeItem: CustomEditTextField) {
        val searchableSpinner = SearchableSpinner(context)
        searchableSpinner.windowTitle = stringsResourceManager.getString(R.string.accountType)
        searchableSpinner.onItemSelectListener = object : OnItemSelectListener {
            override fun setOnItemSelectListener(position: Int, selectedString: String) {
                accountTypeItem.editText.setText(selectedString)
            }
        }
        val list = listOf(AccountItems(EMPTY_STRING, "Parent"),AccountItems(EMPTY_STRING, "Book keeper"))
        searchableSpinner.setAccountTypeSpinnerListItems(list)
        accountTypeItem.editText.keyListener = null
        accountTypeItem.editText.setOnClickListener {
            searchableSpinner.show()
        }
    }

}
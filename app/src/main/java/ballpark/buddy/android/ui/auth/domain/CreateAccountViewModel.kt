package ballpark.buddy.android.ui.auth.domain

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.click_callback.ClickCallback
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.EMPTY_STRING
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.ui.auth.data.User
import ballpark.buddy.android.ui.auth.presentation.CreateAccountFragmentDirections
import ballpark.buddy.android.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
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

    fun showDialogUiSuccessMessage() {
        setUiMessage(
            UIMessage.DialogMessage(
                DialogMessageType.GeneralDialog(
                    GeneralDialogUiData(
                        title = "شکریہ",
                        description = "ہم سے رابطہ کرنے کے لیے",
                        isCancelable = false,
                        primaryButtonText = "Login to continue",
                        primaryButtonListener = object : ClickCallback<Any> {
                            override fun onClick(type: Any) {
                                navigateToLogin()
                            }
                        }
                    )

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

    fun navigateToLogin() {
        navigate(CreateAccountFragmentDirections.actionCreateAccountToLogin())
    }

    private val db = FirebaseFirestore.getInstance()
    fun onTapOfSignupButton(
        mobileNumber: CustomEditTextField,
        password: CustomEditTextField,
        firsNameEditText: CustomEditTextField,
        farmName: CustomEditTextField,
    ) {
        if (mobileNumber.isValid().inverse) {
            mobileNumber.setError("Please enter your mobile number", mobileNumber)
            mobileNumber.requestFocus()
            return
        }
        if (firsNameEditText.isValid().inverse) {
            firsNameEditText.setError("Please enter your full name", firsNameEditText)
            firsNameEditText.requestFocus()
            return
        }
        if (farmName.isValid().inverse) {
            farmName.setError("Please enter your farm name", farmName)
            farmName.requestFocus()
            return
        }
        if (password.isValid().inverse) {
            password.setError("Please enter your password", password)
            password.requestFocus()
            return
        }
        setLoading(true)
        createUser(
            fullName = firsNameEditText.getFieldText(),
            farmName = farmName.getFieldText(),
            mobile = mobileNumber.getFieldText(),
            password = password.getFieldText()
        )
    }

    private fun createUser(
        fullName: String,
        mobile: String,
        password: String,
        farmName: String,
    ) {
        val user = User(
            Constants.getCurrentUnixTimestamp(),
            fullName = fullName,
            mobile = mobile,
            farmName = farmName,
            fcmToken = EMPTY_STRING,
        )
        registerUser(mobile, password, user) { success, errorMessage ->
            setLoading(false)
            if (errorMessage == null) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                sharedPreferencesManager.setUserId(userId)
                _accountCreatedSuccess.value = Pair(success, errorMessage)
            } else {
                showDialogUiMessage(errorMessage)
            }
        }
    }

//    private fun getUserData(userId: String) {
//        db.collection(Constants.USER_TABLE_STAGE).document(userId)
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val document = task.result
//                    if (document != null && document.exists()) {
//                        val userData = document.toObject(User::class.java)
//                        sharedPreferencesManager.setUserObject(userData)
//                        navigate(CreateAccountFragmentDirections.actionCreateAccountToHome())
//                    } else {
//                        showDialogUiMessage("User not found")
//                    }
//                }
//                setLoading(false)
//            }
//    }

    private fun registerUser(
        email: String,
        password: String,
        user: User,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword("$email@gmail.com", password)
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


}
package ballpark.buddy.android.ui.auth.domain

import android.graphics.drawable.Drawable
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.auth.data.User
import ballpark.buddy.android.ui.auth.presentation.LoginFragmentDirections
import ballpark.buddy.android.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : BaseViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun showDialogUiMessageForEmail() {
        setUiMessage(
            UIMessage.DialogMessage(
                DialogMessageType.GeneralDialog(
                    GeneralDialogUiData(
                        title = stringsResourceManager.getString(R.string.email_address),
                        description = stringsResourceManager.getString(R.string.email_point),
                        primaryButtonText = stringsResourceManager.getString(R.string.ok),
                        isCancelable = true
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

    fun navigateToCreateAccount() {
        navigate(LoginFragmentDirections.navLoginToCreateAccount())
    }

//    fun navigateToForgetPassword() {
//        navigate(LoginFragmentDirections.navLoginToForgetPassword())
//    }

    private fun navigateToHome() {
        navigate(LoginFragmentDirections.navLoginToHome())
    }


    fun onTapOfLoginButton(email: CustomEditTextField, password: CustomEditTextField) {
        if (email.isValid().inverse) {
            email.setError("PLease enter you email address", email)
            return
        }
        if (password.isValid().inverse) {
            password.setError("PLease enter your password", password)
            return
        }
        setLoading(true)
        loginUser(
            email = email.getFieldText(),
            password = password.getFieldText()
        ) { success, message ->
            if (success) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                sharedPreferencesManager.setUserId(userId)
                getUserData(userId)
            } else {
                setLoading(false)
                showErrorMessage(message)
            }
        }
    }

    private fun showErrorMessage(message: String?) {
        setUiMessage(
            UIMessage.DialogMessage(
                DialogMessageType.Error(
                    message = stringsResourceManager.getString(R.string.LoginError),
                    description = message,
                    isCancelable = false
                )
            )
        )
    }

    private fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    val exception = task.exception as? FirebaseAuthException
                    onComplete(false, exception?.message ?: "Login failed.")
                }
            }
    }

    private fun getUserData(userId: String) {
        firestore.collection(Constants.USER_TABLE_STAGE).document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        val userData = document.toObject(User::class.java)
                        sharedPreferencesManager.setUserObject(userData)
                        navigateToHome()
                    }else{
                        showErrorMessage("User not found")
                    }
                }
                setLoading(false)
            }
    }




}
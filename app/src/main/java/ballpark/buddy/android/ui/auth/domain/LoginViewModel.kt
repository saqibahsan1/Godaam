package ballpark.buddy.android.ui.auth.domain

import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.auth.presentation.LoginFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
    private val sharedPreferencesManager: SharedPreferencesManager,
) : BaseViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
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

    fun navigateToCreateAccount() {
        navigate(LoginFragmentDirections.navLoginToCreateAccount())
    }

//    fun navigateToForgetPassword() {
//        navigate(LoginFragmentDirections.navLoginToForgetPassword())
//    }

    private fun navigateToHome() {
//        navigate(LoginFragmentDirections.navLoginToHome())
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
                navigateToHome()
            } else {
                showErrorMessage(message)
            }
            setLoading(false)
        }
    }

    private fun showErrorMessage(message: String?){
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


}
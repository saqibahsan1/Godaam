package ballpark.buddy.android.ui.auth.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.resources.DrawableResourceManager
import ballpark.buddy.android.resources.StringsResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
    private val drawableResourceManager: DrawableResourceManager,
) : BaseViewModel() {

    val accountCreatedSuccess: LiveData<Boolean>
        get() = _accountCreatedSuccess
    private val _accountCreatedSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

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

    fun onTapOfSignupButton(email: CustomEditTextField, password: CustomEditTextField, confirmPassword: CustomEditTextField) {
        if (email.isValid().inverse) {
            email.setError("Please enter you email address", email)
            return
        }
        if (password.isValid().inverse) {
            password.setError("Please enter your password", password)
            return
        }
        if (password.getFieldText() != confirmPassword.getFieldText()){
            confirmPassword.setError("password did not matched", confirmPassword)
            return
        }
    }


    private fun accountCreatedSuccessfully() {
        _accountCreatedSuccess.value = true
    }
}
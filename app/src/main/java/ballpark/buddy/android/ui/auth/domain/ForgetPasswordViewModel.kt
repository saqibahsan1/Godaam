package ballpark.buddy.android.ui.auth.domain

import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DialogMessageType
import ballpark.buddy.android.base.data.UIMessage
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.resources.StringsResourceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager,
) : BaseViewModel() {

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

    fun onTapOfGetCode(email: CustomEditTextField) {
        if (email.isValid().inverse) {
            email.setError("Please enter your registered email", email)
            return
        }
    }


}
package qiwa.gov.sa.click_callback

import android.content.Context
import qiwa.gov.sa.editText.CustomEditTextField

@FunctionalInterface
interface ClickCallback<in T> where T : Any {
    fun onClick(type: T)
}

@FunctionalInterface
interface NoInternetClickHandler {
    fun canDismiss(): Boolean
    fun onClick()
}

internal val EMPTY_CLICK_CALL_BACK = object : ClickCallback<Any> {
    override fun onClick(type: Any) {
        // no-op
    }
}
@FunctionalInterface
interface LoginButtonCallback {
    fun onClick(
            email: String,
            password: String?
    )
}

@FunctionalInterface
interface ConfirmPasswordValidation {

    fun onError(context: Context, error: Int, passwordField: CustomEditTextField)

    fun onClick(
        password1: String?,
        passwordConfirm: String?,
    )
}

@FunctionalInterface
interface PasswordValidation {
    fun onError(context:Context,error:Int)
    fun onClick(
        password1: String?
    )
}

@FunctionalInterface
interface PageSelectionCallback {
    fun onPageSelected(pageNumber: Int)
}

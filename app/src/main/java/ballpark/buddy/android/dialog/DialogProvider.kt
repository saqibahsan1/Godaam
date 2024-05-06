package ballpark.buddy.android.dialog

import android.content.Context
import ballpark.buddy.android.click_callback.ClickCallback
import javax.inject.Inject

interface DialogProvider {
    fun showGeneralDialog(context: Context, generalDialogUiData: GeneralDialogUiData)
    fun showNoInternetDialog(context: Context, noInternetDialogUiData: NoInternetDialogUiData)
    fun showErrorDialog(
        context: Context,
        message: String,
        description: String? = null,
        clickCallback: ClickCallback<Any>? = null,
        cancelable: Boolean
    )
}

class DefaultDialogProvider @Inject constructor(
    private val generalDialogProvider: GeneralDialogProvider,
    private val noInternetDialogProvider: NoInternetDialogProvider,
    private val errorDialogProvider: ErrorDialogProvider,
) : DialogProvider {


    override fun showNoInternetDialog(context: Context, noInternetDialogUiData: NoInternetDialogUiData) {
        noInternetDialogProvider.show(context, noInternetDialogUiData)
    }
    override fun showGeneralDialog(context: Context, generalDialogUiData: GeneralDialogUiData) {
        generalDialogProvider.show(context, generalDialogUiData)
    }
    override fun showErrorDialog(
        context: Context,
        message: String,
        description: String?,
        clickCallback: ClickCallback<Any>?,
        cancelable: Boolean
    ) {
        errorDialogProvider.show(context, message, description, clickCallback = clickCallback, isCancelable = cancelable)
    }
}

package qiwa.gov.sa.dialog

import android.content.Context
import qiwa.gov.sa.click_callback.ClickCallback
import javax.inject.Inject

interface DialogProvider {
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
    private val noInternetDialogProvider: NoInternetDialogProvider,
) : DialogProvider {


    override fun showNoInternetDialog(context: Context, noInternetDialogUiData: NoInternetDialogUiData) {
        noInternetDialogProvider.show(context, noInternetDialogUiData)
    }

    override fun showErrorDialog(
        context: Context,
        message: String,
        description: String?,
        clickCallback: ClickCallback<Any>?,
        cancelable: Boolean
    ) {
//        TODO("Not yet implemented")
    }
}

package ballpark.buddy.android.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DismissListener
import ballpark.buddy.android.click_callback.NoInternetClickHandler
import ballpark.buddy.android.databinding.DialogNoInternetBinding
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.extentions.ifEmpty
import javax.inject.Inject
import javax.inject.Singleton

@Parcelize
data class NoInternetDialogUiData(
    val buttonText: String? = null,
    val callback: @RawValue NoInternetClickHandler = object : NoInternetClickHandler {
        override fun canDismiss(): Boolean = true

        override fun onClick() {
            // no-op
        }
    }
) : Parcelable
@Singleton
interface NoInternetDialogProvider {
    fun show(context: Context, noInternetDialogUiData: NoInternetDialogUiData)
}

class DefaultNoInternetDialogProvider @Inject constructor(
) : NoInternetDialogProvider {
    override fun show(context: Context, noInternetDialogUiData: NoInternetDialogUiData) {
        val binding = DialogNoInternetBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        binding.doneButton.text = noInternetDialogUiData.buttonText?.ifEmpty("Okay")
        binding.dismissListener = object : DismissListener {
            override fun onDismiss(value: Any?) {
                value?.let {
                    noInternetDialogUiData.callback.onClick()
                    if (noInternetDialogUiData.callback.canDismiss().default) {
                        alertDialog.dismiss()
                    }
                }
            }
        }
        alertDialog.show()
    }
}

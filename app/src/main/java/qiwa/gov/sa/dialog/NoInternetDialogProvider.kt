package qiwa.gov.sa.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import qiwa.gov.sa.R
import qiwa.gov.sa.base.data.DismissListener
import qiwa.gov.sa.click_callback.NoInternetClickHandler
import qiwa.gov.sa.databinding.DialogNoInternetBinding
import qiwa.gov.sa.extentions.default
import qiwa.gov.sa.extentions.ifEmpty
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

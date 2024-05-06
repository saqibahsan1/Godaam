package ballpark.buddy.android.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import ballpark.buddy.android.R
import ballpark.buddy.android.base.data.DismissListener
import ballpark.buddy.android.click_callback.ClickCallback
import ballpark.buddy.android.databinding.DialogErrorBinding
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.extentions.hide
import ballpark.buddy.android.extentions.ifEmpty
import ballpark.buddy.android.extentions.setTextOrHideIfEmpty
import javax.inject.Inject

interface ErrorDialogProvider {
    fun show(
        context: Context,
        title: String,
        message: String? = null,
        btnText: String? = null,
        data: Any? = null,
        isCancelable: Boolean = true,
        clickCallback: ClickCallback<Any>? = null
    )

    fun hide()
}

class DefaultErrorDialogProvider @Inject constructor(
) : ErrorDialogProvider {
    lateinit var alertDialog: AlertDialog
    override fun show(
        context: Context,
        title: String,
        message: String?,
        btnText: String?,
        data: Any?,
        isCancelable: Boolean,
        clickCallback: ClickCallback<Any>?
    ) {
        val binding = DialogErrorBinding.inflate(LayoutInflater.from(context))
        if (::alertDialog.isInitialized && alertDialog.isShowing) return
        alertDialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(isCancelable)
            .create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        val width = (context.resources.displayMetrics.widthPixels * 0.20)
        alertDialog.window?.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.dismissListener = object : DismissListener {
            override fun onDismiss(value: Any?) {
                alertDialog.dismiss()
                if (data != null) {
                    clickCallback?.onClick(data)
                } else {
                    clickCallback?.onClick(Unit)
                }
            }
        }
        binding.apply {
            titleText.text = title.ifEmpty {
                titleText.hide()
                ""
            }
            descriptionText.setTextOrHideIfEmpty(message)
            doneButton.text = btnText.default().ifEmpty("Okay")
        }
        if (alertDialog.isShowing)
            alertDialog.dismiss()
        else
            alertDialog.show()
    }

    override fun hide() {
        if (::alertDialog.isInitialized) {
            alertDialog.cancel()
            alertDialog.dismiss()
        }

    }
}

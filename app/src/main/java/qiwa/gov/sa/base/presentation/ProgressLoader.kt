package qiwa.gov.sa.base.presentation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import qiwa.gov.sa.R

class ProgressLoader constructor(context: Context) : Dialog(context, false, null) {

    init {
        initViews()
    }

    private fun initViews() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_loader)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun showOrHideProgress(show: Boolean) {
        if (show) show() else dismiss()
    }
}

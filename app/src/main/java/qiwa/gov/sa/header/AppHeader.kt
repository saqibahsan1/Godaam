package qiwa.gov.sa.header

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import qiwa.gov.sa.R
import qiwa.gov.sa.databinding.AppHeaderBinding
import qiwa.gov.sa.extentions.getBinding

class AppHeader @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleRes: Int = 0) :
    ConstraintLayout(context, attributeSet, defStyleRes) {

    private val binding: AppHeaderBinding = getBinding(R.layout.app_header)

    fun setUiData(config: HeaderConfig) {
        binding.run {
            data = config
            executePendingBindings()
        }
    }
}

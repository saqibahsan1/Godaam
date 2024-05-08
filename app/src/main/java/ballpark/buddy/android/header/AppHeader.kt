package ballpark.buddy.android.header

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import ballpark.buddy.android.R
import ballpark.buddy.android.databinding.AppHeaderBinding
import ballpark.buddy.android.extentions.getBinding

class
AppHeader @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleRes: Int = 0) :
    ConstraintLayout(context, attributeSet, defStyleRes) {

    private val binding: AppHeaderBinding = getBinding(R.layout.app_header)

    fun setUiData(config: HeaderConfig) {
        binding.run {
            data = config
            executePendingBindings()
        }
    }
}

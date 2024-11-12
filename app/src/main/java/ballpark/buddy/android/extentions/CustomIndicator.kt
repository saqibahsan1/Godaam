package ballpark.buddy.android.extentions

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import ballpark.buddy.android.R

class CustomIndicator @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleRes: Int = 0
) : LinearLayout(context, attributeSet, defStyleRes) {

    init {
        gravity = Gravity.CENTER
        orientation = HORIZONTAL
    }

    fun addIndicators(size: Int) {
        removeAllViews()
        for (i in 0 until size) {
            addView(View(context))
        }
        updateView(0)
    }

    fun updateView(selectedIndex: Int) {
        for (index in 0..childCount) {
            getChildAt(index)?.let { view ->
                if (index == selectedIndex){
                    view.updateLayoutParams<MarginLayoutParams> {
                        width = dimensionInteger(com.intuit.sdp.R.dimen._12sdp)
                        height = dimensionInteger(com.intuit.sdp.R.dimen._4sdp)
                        marginStart = dimensionInteger(com.intuit.sdp.R.dimen._4sdp)
                        marginEnd = dimensionInteger(com.intuit.sdp.R.dimen._4sdp)
                    }

                }else{
                    view.updateLayoutParams<MarginLayoutParams> {
                        width = dimensionInteger(com.intuit.sdp.R.dimen._5sdp)
                        height = dimensionInteger(com.intuit.sdp.R.dimen._5sdp)
                        marginStart = dimensionInteger(com.intuit.sdp.R.dimen._4sdp)
                        marginEnd = dimensionInteger(com.intuit.sdp.R.dimen._4sdp)
                    }
                }
                view.background =   if(index == selectedIndex) drawable(R.drawable.active_background) else drawable(R.drawable.non_active_background)
            }
        }
    }
}
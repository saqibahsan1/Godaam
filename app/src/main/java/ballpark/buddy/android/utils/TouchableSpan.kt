package ballpark.buddy.android.utils

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class TouchableSpan(
    private val textColor: Int,
    private val pressedTextColor: Int,
    private val isUnderline: Boolean = false
) : ClickableSpan() {

    private var isPressed: Boolean = false

    override fun updateDrawState(paint: TextPaint) {
        super.updateDrawState(paint)
        paint.run {
            color = if (isPressed) pressedTextColor else textColor
            isUnderlineText = isUnderline
            bgColor = Color.TRANSPARENT
        }
    }

    fun setPressed(pressed: Boolean) {
        isPressed = pressed
    }
}

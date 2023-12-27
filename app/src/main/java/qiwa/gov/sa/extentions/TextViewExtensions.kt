package qiwa.gov.sa.extentions

import android.graphics.Paint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import qiwa.gov.sa.utils.Constants
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit


@BindingAdapter("set_text_or_hide_if_empty", "make_invisible_if_empty", requireAll = false)
fun TextView?.setTextOrHideIfEmpty(text: String? = "", invisible: Boolean = false) {
    this?.text = text
    viewVisibility(this, invisible = invisible, show = text?.isNotEmpty().default)
}

fun TextView.applyColorSizeAndFont(
    @FontRes fontPath: Int,
    @ColorRes color: Int,
    @DimenRes size: Int
) {
    applyFont(fontPath)
    applyTextColor(color)
    setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(size))
}

@BindingAdapter(value = ["apply_text_size"])
fun TextView.applyTextSize(@DimenRes dimenRes: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension(dimenRes))
}

@BindingAdapter(value = ["apply_text_color"])
fun TextView.applyTextColor(@ColorRes colorRes: Int) {
    setTextColor(ContextCompat.getColor(context, colorRes))
}

fun TextView.applyFont(@FontRes fontPath: Int) {
    typeface = ResourcesCompat.getFont(context, fontPath)
}

var TextView.drawableStart: Drawable?
    get() = compoundDrawablesRelative[0]
    set(value) = setDrawables(leftDrawable = value)

var TextView.drawableEnd: Drawable?
    get() = compoundDrawablesRelative[2]
    set(value) = setDrawables(rightDrawable = value)

fun TextView.setDrawables(
    leftDrawable: Drawable? = null,
    topDrawable: Drawable? = null,
    rightDrawable: Drawable? = null,
    bottomDrawable: Drawable? = null,
) =
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        leftDrawable,
        topDrawable,
        rightDrawable,
        bottomDrawable
    )

@BindingAdapter("add_underline")
fun TextView.addUnderline(text:String) {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
   // paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

@BindingAdapter("strikethrough")
fun AppCompatTextView.strikethrough(show: Boolean) {
    this.paintFlags = if (show) {
        this.paintFlags or STRIKE_THRU_TEXT_FLAG
    } else {
        this.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
    }
}

fun TextView.transformToHtml(text: String?) {
    setText(HtmlCompat.fromHtml(text.toString(), HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_DIV))
}

fun TextView.makeClickableHtmlText(
    spannableString: SpannableString,
) {
    text = spannableString
    movementMethod = LinkMovementMethod.getInstance()
}

@BindingAdapter(value = ["enable_scrolling"])
fun AppCompatTextView.enableScrolling(enableScrolling: Boolean = true) {
    movementMethod = if (enableScrolling) ScrollingMovementMethod() else null
}

@BindingAdapter("set_movement_method")
fun TextView.attachMovementMethod(
    method: MovementMethod
) {
    movementMethod = method
}

@BindingAdapter("show_timer", "set_count_down_timer", "millis_in_future", "count_down_interval", "keep_minutes_formatting", requireAll = false)
fun TextView.asCountDownTimer(
    callbackToShow: View.OnClickListener? = null,
    callback: View.OnClickListener,
    millisInFuture: Long = Constants.OTP_COUNT_DOWN_TIMER,
    countDownInterval: Long = Constants.COUNT_DOWN_TIMER_INTERVAL,
    keepMinutesFormatting: Boolean = false,


    ) {
    object : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            callbackToShow?.onClick(this@asCountDownTimer)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            text = when {
                minutes >= 60 -> {
                    String.format(
                        Locale.US,
                        "%02d : %02d : %02d ",
                        TimeUnit.MINUTES.toHours(minutes),
                        minutes - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutes)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    )
                }
                minutes in 1..59 || keepMinutesFormatting -> {
                    String.format(
                        Locale.US,
                        "%02d : %02d",
                        minutes,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    )
                }
                else -> {
                    String.format(
                        Locale.US,
                        "%02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    )
                }
            }

            Timber.i("Timer run ${text}")
        }

        override fun onFinish() {
            callback.onClick(this@asCountDownTimer)
            text = String.format(
                Locale.US,
                "%02d : %02d",
                0,
                0 - 0
            )
            Timber.i("Timer done")
        }
    }.start()
}



@FunctionalInterface
interface TimePickerSelection {
    fun onPickTime(hour: Int, minute: Int)
}

@BindingAdapter("set_translation_text", "fallback_text", requireAll = true)
fun TextView.setTranslationText(
    translationText: String?,
    fallbackText: String?
) {
    text = translationText.ifEmpty(fallbackText.default)
}

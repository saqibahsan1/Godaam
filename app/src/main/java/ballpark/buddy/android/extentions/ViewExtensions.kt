package ballpark.buddy.android.extentions

import android.animation.Animator
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ballpark.buddy.android.R
import ballpark.buddy.android.databinding.ItemExpandableHeaderLayoutBinding
import ballpark.buddy.android.databinding.ItemFaqExpandableLayoutBinding
import ballpark.buddy.android.utils.ExpandableLayout
import timber.log.Timber
import java.util.Locale


fun View.show() = run {
    visibility = View.VISIBLE
    this
}

fun View.invisible() = run {
    visibility = View.INVISIBLE
    this
}

fun View.hide() = run {
    visibility = View.GONE
    this
}


@BindingAdapter("set_bottom_menu")
fun BottomNavigationView.setMenu(currentMenu: Int) {
    menu.clear()
    inflateMenu(currentMenu)
}

@BindingAdapter(value = ["view_visibility"])
fun View.viewVisibility(show: Boolean) = run {
    if (show) show() else hide()
}

@BindingAdapter("set_text_if_not_blank")
fun AppCompatEditText.setTextIfNotBlank(enteredText: String?) {
    isEnabled = true
    enteredText?.let {
        if (it.isNotEmpty()) {
            setText(it)
            isEnabled = false
        }
    }
}

@BindingAdapter(value = ["inverse_view_visibility"])
fun View.inverseViewVisibility(hide: Boolean) = run {
    viewVisibility(hide.inverse)
}

@BindingAdapter(value = ["disableField"])
fun View.disableField(disable: Boolean) {
    if (disable) {
        isEnabled = false
        alpha = 0.5f
    } else {
        isEnabled = true
        alpha = 1f
    }
}


@BindingAdapter(value = ["invisible_view_visibility"])
fun View.invisibleViewVisibility(show: Boolean) = run {
    if (show) show() else invisible()
}


fun View.visibility(show: Boolean, invisible: Boolean = false) = run {
    if (show) show() else if (invisible) invisible() else hide()
}

inline fun <T : View> viewVisibility(
        view: T?,
        show: Boolean,
        invisible: Boolean = false,
        crossinline showedCallBack: T.() -> Unit = {},
        crossinline hideCallBack: T.() -> Unit = {}
) =
        view?.run {
            if (show) show() else if (invisible) invisible() else hide()
            if (show) showedCallBack(this) else hideCallBack(this)
        }

fun View.drawable(@DrawableRes drawableRes: Int): Drawable? =
        AppCompatResources.getDrawable(context, drawableRes)

fun View.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun View.dimensionInteger(@DimenRes dimenRes: Int) = dimension(dimenRes).toInt()

fun View.string(@StringRes stringRes: Int) = resources.getString(stringRes)

fun View.colorStateList(@ColorRes color: Int): ColorStateList =
        AppCompatResources.getColorStateList(context, color)

fun View.colorStateList(hexColor: String?): ColorStateList {
    return try {
        ColorStateList.valueOf(Color.parseColor(hexColor.default))
    } catch (e: Exception) {
        colorStateList(R.color.title_bar_color)
    }
}

fun View.isValidResourceId(id: Int) =
    isValidDrawableRes(id) or isValidColorRes(id) or
            isValidDimens(id) or isValidString(id) or
            id.greaterThan(0)

fun View.translate(value: Float = 800f, duration: Long = 2000, flipOnEnd: Boolean = false, onEnd: () -> Unit) {
    animate().translationX(value).setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}

        override fun onAnimationEnd(animation: Animator) {
            scaleX = if (flipOnEnd && scaleX == 1f) {
                -1f
            } else {
                1f
            }
            onEnd()
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }).setDuration(duration).start()
}

fun View.color(@ColorRes color: Int) = resources.getColor(color)

fun View.font(@FontRes font: Int) = ResourcesCompat.getFont(context, font)

fun ViewGroup.inflate(@LayoutRes resourceId: Int): View = View.inflate(context, resourceId, this)

val View.inflater
    get() = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun <T : ViewDataBinding> ViewGroup.getBinding(@LayoutRes resourceId: Int): T =
        DataBindingUtil.inflate(inflater, resourceId, this, true)

fun View.getResourceValueId(a: TypedArray, styleableId: Int) = TypedValue().also {
    a.getValue(styleableId, it)
}.resourceId

fun View.getResourceValue(a: TypedArray, styleableId: Int) = TypedValue().apply {
    a.getValue(styleableId, this)
}


fun View.isValidFont(id: Int) = try {
    font(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidString(id: Int) = try {
    string(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidDimens(id: Int) = try {
    dimension(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidDrawableRes(id: Int) = try {
    drawable(id)
    true
} catch (e: Exception) {
    false
}

fun View.isValidColorRes(id: Int) = try {
    color(id)
    true
} catch (e: Exception) {
    false
}

val View.isVisible
    get() = visibility == View.VISIBLE

val View.isGone
    get() = visibility == View.GONE

val View.isInvisible
    get() = visibility == View.INVISIBLE

inline fun View.isVisible(crossinline showed: View.() -> Unit) = run {
    if (isVisible) showed(this)
}

inline fun View.isGone(crossinline showed: View.() -> Unit) = run {
    if (isGone) showed(this)
}

inline fun View.isInvisible(crossinline showed: View.() -> Unit) {
    if (isInvisible) showed(this)
}

private fun <T : AttributeSet?> View.getStyleAttributes(styleableId: IntArray, t: T): TypedArray =
        context.theme.obtainStyledAttributes(t, styleableId, 0, 0)

fun <T : AttributeSet?> View.getStyleAttributes(
        styleableId: IntArray,
        t: T,
        typedArray: TypedArray.() -> TypedArray
) =
        typedArray(context.theme.obtainStyledAttributes(t, styleableId, 0, 0)).apply {
            recycle()
        }

fun <T : AttributeSet?> ViewGroup.inflateAndGetStyleAttributes(
        @LayoutRes resourceId: Int,
        styleableId: IntArray,
        t: T,
        typedArray: TypedArray.() -> Unit
): View =
        inflate(resourceId).apply {
            typedArray(getStyleAttributes(styleableId, t))
        }

fun <T : AttributeSet?, VB : ViewDataBinding> VB.getStyleAttributesFromBinding(
        styleableId: IntArray,
        t: T,
        typedArray: TypedArray.() -> Unit
): View =
        root.apply {
            typedArray(getStyleAttributes(styleableId, t))
        }

fun View.clickToAction(withDelay: Boolean = true, action: () -> Unit = {}) {
    if (withDelay) {
        setOnClickListener(
                SafeClickListener {
                    hideKeyboard()
                    action()
                }
        )
    } else {
        setOnClickListener {
            hideKeyboard()
            action()
        }
    }
}

fun View.hideKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            0
    )
}

fun View.showKeyboard() {
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}


fun View.createTextView(@DimenRes dimenRes: Int, @ColorRes colorRes: Int, @FontRes font: Int) =
        AppCompatTextView(context).apply {
            applyColorSizeAndFont(font, colorRes, dimenRes)
        }

fun <T : View> View.castTo() = this as T

fun ProgressBar.resetProgress() {
    secondaryProgress = 0
}

fun ProgressBar.setMainProgress(progress: Int) {
    secondaryProgress = progress
}

fun ViewGroup.getFirstChildIfExists(): View? {
    return if (childCount.greaterThan(0)) getChildAt(0) else null
}

fun View.updatePadding(@Px all: Int) {
    updatePadding(all, all, all, all)
}

fun View.disableByApplyingAlpha(enable: Boolean, alphaValue: Float = 0.5f) {
    isEnabled = enable
    if (enable.inverse) {
        alpha = alphaValue
    }
}

@BindingAdapter(value = ["attach_toggle_listener", "title", "description"], requireAll = false)
fun ExpandableLayout.handleFAQsExpandableLayout(
    attachToggleListener: Boolean?,
    titleText: String?,
    descriptionText: String?
) {
    if (attachToggleListener.default) {
        attachCallback(object : ExpandableLayout.Callback {
            override fun <TVB : ViewDataBinding, EVB : ViewDataBinding> onExpanded(
                expanded: Boolean,
                topLayout: TVB?,
                expandableLayout: EVB?
            ) {
                (topLayout as? ItemExpandableHeaderLayoutBinding)?.run {
                    icon.setImageResource(if (expanded) R.drawable.ic_chevron_down else R.drawable.ic_chevron_right)
//                    parent.setBackgroundColor(color(if (expanded) R.color.white else R.color.text_hint_color))
                    title.applyTextColor(R.color.black)
                }
            }
        })
    }
    getTopLayoutBinding<ItemExpandableHeaderLayoutBinding> {
        title.text = titleText
    }
    getExpandableLayoutBinding<ItemFaqExpandableLayoutBinding> {
        content.text = descriptionText
    }
    setDefaultState()
}

fun <V : View> BottomSheetBehavior<V>.toggle(open: Boolean = true) {
    if (open.inverse) collapse() else expand()
}

fun <V : View> BottomSheetBehavior<V>.toggle() {
    if (state == BottomSheetBehavior.STATE_EXPANDED) collapse() else expand()
}

fun <V : View> BottomSheetBehavior<V>.collapse() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}

fun <V : View> BottomSheetBehavior<V>.expand() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

fun <V : View> V.setupBottomSheetBehavior(callback: (bottomSheet: View, newState: Int) -> Unit = { _, _ -> }): BottomSheetBehavior<V> =
        BottomSheetBehavior.from(this).apply {
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    //
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    callback(bottomSheet, newState)
                }
            })
        }

fun View.applyMargin(@Px all: Int) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = all
        topMargin = all
        rightMargin = all
        bottomMargin = all
    }
}

fun View.applyMargin(@Px start: Int = 0, @Px top: Int = 0, @Px end: Int = 0, @Px bottom: Int = 0) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        marginStart = start
        topMargin = top
        marginEnd = end
        bottomMargin = bottom
    }
}

fun Animation.startAnimationOnViews(vararg views: View) {
    views.forEach { view ->
        view.startAnimation(this)
    }
}

fun Bitmap.compressAndGetUri(context: Context, compressedUri: (Uri) -> Unit) {
    context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "title")
                put(MediaStore.Images.Media.MIME_TYPE, "image/*")
            }
    )?.let { uri ->
        try {
            context.contentResolver.openOutputStream(uri)?.let { stream ->
                compress(Bitmap.CompressFormat.JPEG, 70, stream)
                stream.close()
            }
        } catch (e: Exception) {
            Timber.e(e.localizedMessage)
        }
        compressedUri(uri)
    }
}

fun BottomNavigationView.enable(enable: Boolean = true) {
    isEnabled = enable
    menu.forEach { it.isEnabled = enable }
}

fun View.toggleAppearance(enabled: Boolean = true) {
    isEnabled = enabled
}

@BindingAdapter("single_on_click")
fun View.singleOnClick(listener: View.OnClickListener) {
    clickToAction {
        listener.onClick(this)
    }
}

@BindingAdapter("single_on_click_no_delay")
fun View.singleOnClickNoDelay(listener: View.OnClickListener) {
    clickToAction(withDelay = false) {
        listener.onClick(this)
    }
}

@BindingAdapter("background_tint")
fun View.setTint(tint: Int) {
    backgroundTintList = colorStateList(tint)
}
fun View.captureScreenshot(): Bitmap? {
    var screenshot: Bitmap? = null
    try {
        screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        //  screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        draw(canvas)
    } catch (e: Exception) {
        Timber.e("Failed to capture screenshot because:%s", e.message)
    }
    return screenshot
}

fun Long.getFormattedDuration(forceShowHours: Boolean = false): String {
    val sb = StringBuilder(8)
    val hours = this / 3600
    val minutes = this % 3600 / 60
    val seconds = this % 60

    if (this >= 3600) {
        sb.append(String.format(Locale.getDefault(), "%02d", hours)).append(":")
    } else if (forceShowHours) {
        sb.append("0:")
    }

    sb.append(String.format(Locale.getDefault(), "%02d", minutes))
    sb.append(":").append(String.format(Locale.getDefault(), "%02d", seconds))
    return sb.toString()
}

fun WindowManager.getDeviceWidth(): Int {
    val displayMetrics = DisplayMetrics()
    @Suppress("DEPRECATION")
    defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}
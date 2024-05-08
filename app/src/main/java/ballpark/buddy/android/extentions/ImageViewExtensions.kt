package ballpark.buddy.android.extentions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import ballpark.buddy.android.R
import ballpark.buddy.android.header.HeaderRightButtonType


@BindingAdapter("drawable")
fun ImageView.setDrawableFromBinding(drawable: Drawable?) {
    setImageDrawable(drawable)
}

@BindingAdapter("drawable_res")
fun ImageView.setDrawableFromBinding(drawableRes: Int?) {
    if (drawableRes != null && drawableRes != 0) {
        setImageDrawable(drawable(drawableRes))
    } else {
        hide()
    }
}
@BindingAdapter("header_right_icon_drawable_res")
fun ImageView.setDrawableFromBindingForHeaderRightIcon(headerRightButtonType: HeaderRightButtonType?) {
    when (headerRightButtonType) {
        HeaderRightButtonType.Home -> setImageDrawable(drawable(R.drawable.ic_logout))
        else -> {
            //
        }
    }
}

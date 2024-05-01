package ballpark.buddy.android.extentions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter


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

package qiwa.gov.sa.extentions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentContainerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONException
import org.json.JSONObject
import qiwa.gov.sa.header.AppHeader
import qiwa.gov.sa.header.HeaderConfig
import java.lang.reflect.Type

@BindingAdapter("set_ui_data")
fun AppHeader.setUiDataFromBinding(config: HeaderConfig?) {
    config?.let { data ->
        setUiData(data)
    }
}

@BindingAdapter("top_margin")
fun FragmentContainerView.setTopMargin(value: Int?) {
    applyMargin(top = value.default)
}


@BindingAdapter("drawable")
fun ViewGroup.setDrawableFromBinding(drawable: Drawable?) {
    background = drawable ?: drawable(android.R.color.transparent)
}

@BindingAdapter("set_button_drawable")
fun AppCompatCheckBox.setDrawableFromBinding(drawable: Drawable?) {
    buttonDrawable = drawable
}

@BindingAdapter("apply_horizontal_margin")
fun View.horizontalMargin(dimen: Float?) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        marginStart = dimen?.toInt().default
        marginEnd = dimen?.toInt().default
    }
}


@BindingAdapter("apply_horizontal_margin")
fun ViewGroup.horizontalMargin(dimen: Float?) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        marginStart = dimen?.toInt().default
        marginEnd = dimen?.toInt().default
    }
}

@BindingAdapter("apply_padding")
fun View.applyPadding(dimen: Float?) {
    updatePadding(all = dimen?.toInt().default)
}

@BindingAdapter("apply_horizontal_padding")
fun View.horizontalPadding(dimen: Float?) {
    updatePadding(left = dimen?.toInt().default, right = dimen?.toInt().default)
}

@BindingAdapter("apply_horizontal_padding")
fun ViewGroup.horizontalPadding(dimen: Float?) {
    updatePadding(left = dimen?.toInt().default, right = dimen?.toInt().default)
}

fun <T> convertObjectToJsonString(model: T): String =
    GsonBuilder().create().toJson(model)

fun getJson(bundle: Bundle?): String? {
    if (bundle == null) return null
    val jsonObject = JSONObject()
    for (key in bundle.keySet()) {
        try {
            jsonObject.put(key, JSONObject.wrap(bundle.get(key)))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    return jsonObject.toString()
}


fun <T> convertModelToJsonString(model: T): String =
    JSONObject(Gson().toJson(model)).toString()


inline fun <reified T> convertJsonToModel(string: String): T? =
    GsonBuilder()
        .enableComplexMapKeySerialization()
        .setPrettyPrinting()
        .create()
        .fromJson(
            string.replace("^\"|\"$".toRegex(), "").replace("\\n", "")
                .replace("\\", "").trim(),
            T::class.java
        )

inline fun <reified T> parseArray(json: String, typeToken: Type): T {
    val gson = GsonBuilder().create()
    return gson.fromJson(json, typeToken)
}

fun MutableMap<String, Any>?.getPayFortResponseMessage(): String =
    this?.get("response_message").toString()



@BindingAdapter("apply_start_margin")
fun View.applyStartMargin(dimen: Float?) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        marginStart = dimen?.toInt().default
    }
}
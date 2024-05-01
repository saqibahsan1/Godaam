package ballpark.buddy.android.extentions

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable
import java.net.SocketException
import com.intuit.sdp.R.dimen
import ballpark.buddy.android.R
import timber.log.Timber

var toast:Toast? = null
fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) =
    message?.let {
        Toast.makeText(this, message, length).show()
    }

fun Fragment.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) =
    message?.let {
        if (toast != null){
            toast?.cancel()
        }
        toast = Toast.makeText((requireActivity() as AppCompatActivity), message, length)
        toast?.show()
    }

fun Fragment.showSnackBar(
    message: String?,
    duration: Int = Snackbar.LENGTH_LONG,
    backgroundColor: Int = R.color.title_bar_color,
    textColor: Int = R.color.white,
    actionListener: () -> Unit = {},
) = message?.let {
    view?.let {
        Snackbar.make(it, message, duration).apply {
            setBackgroundTintList(colorStateList(backgroundColor))
            setTextColor(color(textColor))
            animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
            setAction(string(R.string.ok)) {
                actionListener()
            }
            setActionTextColor(color(textColor))
        }.show()
    }
}

fun Fragment.inflateLayout(@LayoutRes layoutRes: Int, container: ViewGroup?): View? =
    layoutInflater.inflate(layoutRes, container, false)

fun Fragment.inflateLayout(@LayoutRes layoutRes: Int): View? =
    layoutInflater.inflate(layoutRes, null)

fun Fragment.drawable(@DrawableRes drawableRes: Int): Drawable? =
    AppCompatResources.getDrawable(requireContext(), drawableRes)

fun Fragment.dimensionInInteger(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes).toInt()

fun Fragment.string(@StringRes stringRes: Int) = resources.getString(stringRes)

fun Fragment.colorStateList(@ColorRes color: Int): ColorStateList =
    AppCompatResources.getColorStateList(requireContext(), color)

fun Fragment.color(@ColorRes color: Int) = resources.getColor(color)

fun Fragment.font(@FontRes font: Int) = ResourcesCompat.getFont(requireContext(), font)

inline fun <reified T : Serializable> Fragment.getSerializableArguments(key: String) =
    requireArguments().getSerializable(key) as? T

fun Fragment.getStringArgument(key: String) = requireArguments().getString(key)

fun Fragment.getIntegerArgument(key: String) = requireArguments().getInt(key)

fun <T> Fragment.hasArgument(key: String, has: T.() -> Unit) =
    requireArguments().hasOrNull(key, has)

fun Fragment.navigate(navDirections: NavDirections, navOptions: NavOptions? = null) {
    if (canNavigate(navDirections)) {
        val navController = findNavController()
        navController.navigate(navDirections, navOptions)
    }
}

fun Fragment.navigate(
    resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    findNavController().navigate(resId, args, navOptions, navExtras)
}

fun Fragment.navigateBack() {
    requireActivity().onBackPressed()
//    findNavController().navigateUp()
}

fun <T> Fragment.observeLiveData(liveData: LiveData<T>, callback: (T) -> Unit) {
    liveData.nonNullObserver(viewLifecycleOwner, callback)
}

fun <T> Fragment.nonNullObserver(liveData: LiveData<T?>, callback: (T) -> Unit) {
    liveData.nonNullObserver(viewLifecycleOwner) {
        it?.let(callback)
    }
}

fun Fragment.getAppHeaderHeight() = resources.getDimension(dimen._40sdp).toInt()

fun getDefaultAppHeaderHeight() = 0

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun Fragment.getInteger(@IntegerRes resId: Int): Int = resources.getInteger(resId)


fun Fragment.getLocalIpAddress(): String? {
    try {
        val wifiMgr = requireActivity().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        val wifiInfo = wifiMgr?.connectionInfo
        val ip = wifiInfo?.ipAddress
        Timber.d("RAW IP $ip")
        val ipAddress = Formatter.formatIpAddress(ip.default)
        Timber.d("Formatted $ipAddress")
        return ipAddress
    } catch (ex: SocketException) {
        ex.printStackTrace()
    }
    return null
}

fun Fragment.canNavigate(directions: NavDirections? = null): Boolean {
    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id
    val destinationIdOfThisFragment =
        view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController
    return if (destinationIdInNavController == destinationIdOfThisFragment) {
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        if (directions != null) {
            navController.currentDestination?.getAction(directions.actionId) != null
        } else {
            true
        }
    } else {
        val reason = getString(R.string.navigation_exception)
        Timber.d(reason)
        false
    }
}

fun Fragment.isSameDestination(directions: NavDirections? = null): Boolean {
    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id
    val destinationIdOfThisFragment =
        view?.getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController
    return if (destinationIdInNavController == destinationIdOfThisFragment) {
        view?.setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        if (directions != null) {
            navController.currentDestination?.getAction(directions.actionId) != null
        } else {
            true
        }
    } else {
        val reason = getString(R.string.navigation_exception)
        Timber.d(reason)
        false
    }
}

fun BottomSheetDialogFragment.configureBottomSheet(dialog: Dialog, percentage: Int = 70, enableDragging: Boolean = false) {
    dialog.setOnShowListener { dialogInterface ->
        (dialogInterface as? BottomSheetDialog)
            ?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let {
                val behavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from(it)
                it.layoutParams = it.layoutParams.apply {
                    height = context?.getWindowHeight()?.times(percentage)?.div(100).default
                }
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = enableDragging
                if (enableDragging.inverse) {
                    behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                            //
                        }
                    })
                }
            }
    }
}

fun Context.getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun Context.getWindowWidth(): Int {
    val displayMetrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}


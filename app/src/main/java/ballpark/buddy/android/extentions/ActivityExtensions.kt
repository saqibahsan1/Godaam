package ballpark.buddy.android.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import ballpark.buddy.android.host.MainActivity
import ballpark.buddy.android.R

fun AppCompatActivity.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()

fun AppCompatActivity.hideKeyboard() =
    (getSystemService(android.app.Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

fun AppCompatActivity.openViewer(filePath: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(filePath)
        }
    )
}

fun AppCompatActivity.connectivityManager() =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.getTelephonyManager(): TelephonyManager =
    getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

fun Activity.invertOrientation(): Int {
    return (resources.configuration).apply {
        orientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) Configuration.ORIENTATION_LANDSCAPE else Configuration.ORIENTATION_PORTRAIT
        requestedOrientation = orientation
    }.orientation
}

fun Activity.keepScreenAlive() {
    this.apply {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}


fun AppCompatActivity.navigate(navDirections: NavDirections, navOptions: NavOptions? = null) {
    findNavController(R.id.nav_host_fragment_content_main).navigate(navDirections, navOptions)
}

fun AppCompatActivity.navigate(
    resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    findNavController(R.id.nav_host_fragment_content_main).navigate(resId, args, navOptions, navExtras)
}

fun AppCompatActivity.navigateBack() {
    findNavController(R.id.nav_host_fragment_content_main).navigateUp()
}

fun AppCompatActivity.popBackStack(@IdRes menuId: Int, inclusive: Boolean) {
    findNavController(R.id.nav_host_fragment_content_main).popBackStack(menuId, inclusive)
}

fun AppCompatActivity.navController() =
    findNavController(R.id.nav_host_fragment_content_main)

fun AppCompatActivity.restartHost() {
    startActivity(
        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    )
}

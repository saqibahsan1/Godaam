package ballpark.buddy.android.base.domain

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import ballpark.buddy.android.base.data.ActivityResultContractData
import ballpark.buddy.android.extentions.EMPTY_STRING
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType

interface CustomScreenHeader {

    fun getHeaderConfig(
        background: Drawable? = null,
        title: String = EMPTY_STRING,
        rightButtonType: HeaderRightButtonType = HeaderRightButtonType.None,
        showBackButton: Boolean = true,
    ): HeaderConfig
}

interface DialogDismissalHandler {
    fun onDialogDismissed(value: Any? = null)
}

interface RuntimePermissionListener {
    fun onPermissionGranted(permissions: List<String>)
    fun onLocationPermissionDenied(permissions: List<String>)
}



interface GetImageFromCameraContractProvider {
    val getTakeImageRegistrationContract: ActivityResultContractData.Register<Uri, Boolean>
}

interface GetImageFromGalleryContractProvider {
    val getGalleryImageRegistrationContract: ActivityResultContractData.Register<String, Uri?>
}

interface GetImageFromCameraLauncherProvider {
    val takeImageContractLauncher: ActivityResultLauncher<Uri>
}

interface GetImageFromGalleryLauncherProvider {
    val galleryImageContractLauncher: ActivityResultLauncher<String>
}


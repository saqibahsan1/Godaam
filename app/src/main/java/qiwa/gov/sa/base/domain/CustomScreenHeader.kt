package qiwa.gov.sa.base.domain

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import qiwa.gov.sa.base.data.ActivityResultContractData
import qiwa.gov.sa.extentions.EMPTY_STRING
import qiwa.gov.sa.header.HeaderConfig
import qiwa.gov.sa.header.HeaderRightButtonType

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


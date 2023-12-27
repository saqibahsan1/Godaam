package qiwa.gov.sa.base.data

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import qiwa.gov.sa.base.domain.RuntimePermissionListener

data class RuntimePermissionData(
    val permissions: List<String>,
    val runtimePermissionHandler: RuntimePermissionListener
)

val cameraPermission: List<String> = listOf(
    Manifest.permission.CAMERA
)

val storagePermission: List<String> = listOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

val storageAndCameraPermission: List<String> = listOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.RECORD_AUDIO
)

val locationPermission: List<String> =
    listOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

@RequiresApi(Build.VERSION_CODES.Q)
var backgroundLocationPermission: List<String> =
    listOf(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

val recordingPermission: List<String> =
    listOf(Manifest.permission.RECORD_AUDIO)

@RequiresApi(Build.VERSION_CODES.Q)
val activityRecognition: List<String> =
    listOf(Manifest.permission.ACTIVITY_RECOGNITION)

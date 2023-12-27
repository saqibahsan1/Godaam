package qiwa.gov.sa.base.data

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

sealed class ActivityResultContractData {
    data class Register<I, O>(
        val contract: ActivityResultContract<I, O>,
        val output: O.() -> Unit,
    )

    data class Launch<I>(
        val activityResultLauncher: ActivityResultLauncher<I>,
        val input: I
    )
}

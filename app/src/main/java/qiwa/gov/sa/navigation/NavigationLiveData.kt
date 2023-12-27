package qiwa.gov.sa.navigation

import android.content.Intent
import androidx.annotation.IdRes
import androidx.navigation.NavDirections
import qiwa.gov.sa.base.domain.SingleLiveEvent
import qiwa.gov.sa.base.data.ActivityResultContractData
import javax.inject.Inject

class NavigationLiveData @Inject constructor() : SingleLiveEvent<FragmentNavigator>()

sealed class FragmentNavigator {
    data class Push(val navDirections: NavDirections) : FragmentNavigator()
    data class BottomNavItem(@IdRes val menuItemId: Int) : FragmentNavigator()
    data class ViaIntent(val intent: Intent) : FragmentNavigator()
    data class LaunchActivityResultContract<I>(
        val launchResult: ActivityResultContractData.Launch<I>
    ) : FragmentNavigator()

    data object Pop : FragmentNavigator()
    data object Finish : FragmentNavigator()
}
package ballpark.buddy.android.base.data

import androidx.lifecycle.LiveData
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.navigation.FragmentNavigator
import ballpark.buddy.android.navigation.NavigationLiveData


data class FragmentConfig(
    val headerConfig: HeaderConfig,
    val showFooter: Boolean,
    val fragNavContainerTopMargin: Int,
    val navigationLiveData: LiveData<FragmentNavigator>
) {
    companion object {
        internal val DEFAULT =
            FragmentConfig(
                showFooter = false,
                headerConfig = HeaderConfig.DEFAULT,
                navigationLiveData = NavigationLiveData(),
                fragNavContainerTopMargin = 0
            )
    }
}
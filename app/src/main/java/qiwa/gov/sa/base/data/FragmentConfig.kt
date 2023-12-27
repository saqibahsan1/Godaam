package qiwa.gov.sa.base.data

import androidx.lifecycle.LiveData
import qiwa.gov.sa.header.HeaderConfig
import qiwa.gov.sa.navigation.FragmentNavigator
import qiwa.gov.sa.navigation.NavigationLiveData


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
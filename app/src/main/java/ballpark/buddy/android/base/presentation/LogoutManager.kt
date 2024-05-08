package ballpark.buddy.android.base.presentation

import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.extentions.EMPTY_STRING
import javax.inject.Inject

interface LogoutManager {
    fun logout()
}

class DefaultLogoutManager @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
) : LogoutManager {
    override fun logout() {
        sharedPreferencesManager.setUserId(EMPTY_STRING)
    }
}

package ballpark.buddy.android.cache

import android.content.Context
import androidx.core.content.edit
import ballpark.buddy.android.resources.StringsResourceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ballpark.buddy.network.NetworkPreferencesManager
import ballpark.buddy.android.R
import ballpark.buddy.android.extentions.EMPTY_STRING
import ballpark.buddy.android.extentions.convertJsonToModel
import ballpark.buddy.android.extentions.convertModelToJsonString
import ballpark.buddy.android.extentions.convertObjectToJsonString
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.ui.auth.data.User
import javax.inject.Inject

interface SharedPreferencesManager {
    fun setUserId(userId: String?)
    fun setUserObject(userData: User?)
    fun setEmail(email: String?)
    fun setLocale(locale: String?)
    fun getEmail(): String
    fun getUserId(): String
    fun getUserObject(): User?
    fun removeAuthToken()
    fun isOnBoardingVisited(): Boolean
    fun setOnBoardingVisited(visited: Boolean)
    fun clearSharedPrefs()

}

class DefaultSharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stringsResourceManager: StringsResourceManager,
    private val networkPreferencesManager: NetworkPreferencesManager,
) : SharedPreferencesManager {

    private val sharedPreferences by lazy {
        context.getSharedPreferences(
            stringsResourceManager.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    override fun setUserId(token: String?) {
        networkPreferencesManager.setAuthToken(token)
    }

    override fun setUserObject(userData: User?) {
        val userDataString = convertObjectToJsonString(userData)
        setString(USER_OBJECT, userDataString.default)
    }

    override fun setEmail(email: String?) {
        setString(USER_EMAIL,email.default)
    }

    override fun setLocale(locale: String?) {
        setString(LOCALE,locale.default)
    }

    override fun getEmail(): String =
        getString(USER_EMAIL)

    override fun getUserId(): String =
        networkPreferencesManager.getAuthToken()

    override fun getUserObject(): User? {
        val jsonText =  sharedPreferences.getString(USER_OBJECT, EMPTY_STRING)
        return convertJsonToModel<User>(jsonText?.let { getString(it) }.default)
    }

    override fun removeAuthToken() {
        networkPreferencesManager.removeAuthToken()
    }

    override fun isOnBoardingVisited(): Boolean {
        return getBoolean(IS_ON_BOARDING_VISITED)
    }

    override fun setOnBoardingVisited(visited: Boolean) {
        setBoolean(IS_ON_BOARDING_VISITED, visited)
    }

    override fun clearSharedPrefs() {
        clearPrefs()
    }

    private fun setBoolean(key: String, value: Boolean) =
        sharedPreferences.edit {
            putBoolean(key, value)
        }


    private fun clearPrefs() =
        sharedPreferences.edit().clear().apply()


    private fun getBoolean(key: String): Boolean =
        sharedPreferences.getBoolean(key, false).default

    private fun setString(key: String, value: String) =
        sharedPreferences.edit {
            putString(key, value)
        }

    private fun getString(key: String): String =
        sharedPreferences.getString(key, EMPTY_STRING).default

    companion object {
        private const val IS_ON_BOARDING_VISITED = "IS_ON_BOARDING_VISITED"
        private const val USER_EMAIL = "email"
        private const val USER_OBJECT = "user_object"
        private const val LOCALE = "locale"
    }
}

package ballpark.buddy.android.resources

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

interface StringsResourceManager {
    fun getString(@StringRes id: Int): String
    fun getString(@StringRes id: Int, formattedArg: String): String
    fun getString(@StringRes id: Int, formattedArg: Int): String
    fun getString(@StringRes id: Int, formattedArg: Float): String
    fun getStringArray(@ArrayRes id: Int): Array<out String>
}

@Module
@InstallIn(SingletonComponent::class)
class DefaultStringsResourceManager @Inject constructor(@ApplicationContext private val context: Context) :
    StringsResourceManager {

    override fun getString(@StringRes id: Int) = context.getString(id)

    override fun getString(@StringRes id: Int, formattedArg: String) =
        context.getString(id, formattedArg)

    override fun getString(@StringRes id: Int, formattedArg: Int) =
        context.getString(id, formattedArg)

    override fun getString(@StringRes id: Int, formattedArg: Float) =
        context.getString(id, formattedArg)

    override fun getStringArray(id: Int): Array<out String> =
        context.resources.getStringArray(id)
}

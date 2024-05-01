package ballpark.buddy.android.resources

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface ColorResourceManager {
    fun getColor(@ColorRes resId: Int): Int
}

class DefaultColorResourceManager @Inject constructor(
    @ApplicationContext private val context: Context
) : ColorResourceManager {

    override fun getColor(resId: Int): Int = ContextCompat.getColor(context, resId)
}

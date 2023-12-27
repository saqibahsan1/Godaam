package qiwa.gov.sa.resources

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

interface DrawableResourceManager {
    fun getDrawable(@DrawableRes resId: Int): Drawable?
}

@Module
@InstallIn(SingletonComponent::class)
class DefaultDrawableResourceManager @Inject constructor() : DrawableResourceManager {
    @Inject
    @ApplicationContext
    lateinit var context: Context

    override fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }
}


package qiwa.gov.sa.base.domain

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.LocaleList
import qiwa.gov.sa.extentions.greaterThan
import qiwa.gov.sa.extentions.greaterThanEqualsTo
import java.util.*

@Suppress("DEPRECATION")
class ContextWrapper(base: Context?) : android.content.ContextWrapper(base) {
    companion object {
        @SuppressLint("NewApi")
        fun wrap(context: Context?, newLocale: Locale?): ContextWrapper {
            var contextLocal = context
            val res = context?.resources
            val configuration = res?.configuration
            if (isVersionGreaterThan(24, true)) {
                configuration?.setLocale(newLocale)
                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                configuration?.setLocales(localeList)
                contextLocal = context?.createConfigurationContext(configuration!!)
            } else if (isVersionGreaterThan(17, true)) {
                configuration?.setLocale(newLocale)
                contextLocal = context?.createConfigurationContext(configuration!!)
            } else {
                configuration?.locale = newLocale
                res?.updateConfiguration(configuration, res.displayMetrics)
            }
            return ContextWrapper(contextLocal)
        }
    }
}

fun isVersionGreaterThan(version: Int = 24, equals: Boolean = false) =
    if (equals) Build.VERSION.SDK_INT.greaterThanEqualsTo(version) else Build.VERSION.SDK_INT.greaterThan(version)

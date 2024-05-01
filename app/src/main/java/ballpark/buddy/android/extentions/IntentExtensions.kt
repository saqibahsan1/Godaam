@file:Suppress("UNCHECKED_CAST")

package ballpark.buddy.android.extentions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore

fun <T> Intent.get(string: String) = extras?.get(string) as? T

fun <T> Intent.hasDataWithKey(key: String) = hasExtra(key) && get<T>(key) != null

fun <T : Any?> Bundle.hasOrNull(key: String, has: T.() -> Unit) = get(key)?.let { has(get(key) as T) }

fun Intent.putFileProviderUri(photoURI: Uri): Intent {
    putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
    return this
}

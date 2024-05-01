package ballpark.buddy.android.extentions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.nonNullObserver(lifecycleOwner: LifecycleOwner, observer: (t: T) -> Unit) {
    observe(
        lifecycleOwner
    ) {
        it?.let {
            observer(it)
        }
    }
}

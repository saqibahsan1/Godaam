package ballpark.buddy.android.extentions



val Boolean.inverse
    get() = not()

val Boolean?.toString
    get() = if (this == null || this == false) "0" else "1"

val Boolean?.default
    get() = this ?: false

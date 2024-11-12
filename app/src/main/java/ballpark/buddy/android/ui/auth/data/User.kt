package ballpark.buddy.android.ui.auth.data

data class User(
    var createDate: Long? = null,
    var mobile: String? = null,
    var fcmToken: String? = null,
    @field:JvmField
    var fullName: String? = null,
    var farmName: String? = null,
    var userId: String? = null,
)
package ballpark.buddy.android.ui.auth.data

data class User(
    var createDate: Long? = null,
    var email: String? = null,
    var fcmToken: String? = null,
    @field:JvmField
    var firstName: String? = null,
    var lastName: String? = null,
    var zipCode: String? = null,
    var paymentType: String? = null,
    var accountType: String? = null,
    var league: String? = null,
    var userId: String? = null,
)
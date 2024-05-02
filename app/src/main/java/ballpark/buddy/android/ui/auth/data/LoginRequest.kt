package ballpark.buddy.android.ui.auth.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
val email: String,
val password: String? = null,
val token: String? = null
) : Parcelable

@Parcelize
data class ResetPassword(
val email: String,
val password: String? = null,
val accessToken: String? = null
) : Parcelable

@Parcelize
data class ChangeEmailRequest(
val email: String,
val accessToken: String? = null
) : Parcelable
@Parcelize
data class ChangePasswordRequest(
val oldPassword: String,
val newPassword: String,
val accessToken: String? = null
) : Parcelable


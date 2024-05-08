package ballpark.buddy.android.ui.home.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeUiData(
    var postTime: Long? = null,
    var postedDisplayTime: String? = null,
    var field: String? = null,
    var bookKeeper: String? = null,
    var gameId: String? = null,
    var isNewUpdates: Boolean? = false,
    @field:JvmField
    var jobDuty: String? = null,
    var leagueName: String? = null,
    var parent: String? = null,
    var payAmount: String? = null,
    var postedBy: String? = null,
    var postedByName: String? = null,
    var streetAddress: String? = null,
    var team: String? = null,
): Parcelable
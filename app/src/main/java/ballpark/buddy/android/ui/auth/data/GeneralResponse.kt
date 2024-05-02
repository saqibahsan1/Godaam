package ballpark.buddy.android.ui.auth.data

import ballpark.buddy.network.BaseResponse


data class GeneralResponse(
    override val response: Nothing?,
    override val status: Int,
    override val message: String
) : BaseResponse<Nothing>()

data class StringResponse(
    override val response: Nothing?,
    override val status: Int,
    override val message: String
) : BaseResponse<String>()

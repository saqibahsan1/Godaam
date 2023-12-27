package qiwa.gov.network

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

suspend inline fun <T> callApi(crossinline apiFunction: suspend () -> T): ApiResult<T> =
    try {
        ApiResult.Success(apiFunction.invoke())
    } catch (exception: Exception) {
        when (exception) {
            is IOException, is UnknownHostException -> ApiResult.getNetworkFailure()
            is HttpException -> ApiResult.getFailureFromException(exception)
            else -> ApiResult.technicalFailure()
        }
    }

suspend fun <T> ApiResult<BaseResponse<T>>.unZipWithData(
    successBlock: suspend (T) -> Unit,
    failureBlock: suspend (ErrorWithCode) -> Unit
) {
    when (this) {
        is ApiResult.Failure -> failureBlock(ErrorWithCode(message = error.message, error.statusCode))
        is ApiResult.Success ->
            if (data.status == 200) {
                data.response?.let {
                    successBlock(it)
                } ?: failureBlock(ErrorWithCode(message = data.message, data.status))
            } else {
                failureBlock(ErrorWithCode(message = data.message, data.status))
            }
    }
}
suspend fun <T> ApiResult<BreakDownResponse<T>>.unZipWithDataBreakDown(
    successBlock: suspend (String) -> Unit,
    failureBlock: suspend (ErrorWithCode) -> Unit
) {
    when (this) {
        is ApiResult.Failure -> failureBlock(ErrorWithCode(message = error.message, error.statusCode))
        is ApiResult.Success ->
            if (data.succeeded) {
                successBlock(data.entryPoint)
            } else {
                failureBlock(ErrorWithCode(message = data.message, 401))
            }
    }
}

suspend fun <T> ApiResult<BaseResponse<T>>.unZipWithNullableData(
    successBlock: suspend (T?) -> Unit,
    failureBlock: suspend (ErrorWithCode) -> Unit
) {
    when (this) {
        is ApiResult.Failure -> failureBlock(ErrorWithCode(message = error.message, error.statusCode))
        is ApiResult.Success ->
            if (data.status == 200) {
                successBlock(data.response)
            } else {
                failureBlock(ErrorWithCode(message = data.message, data.status))
            }
    }
}

data class ErrorWithCode(
    val message: String,
    val code: Int
)

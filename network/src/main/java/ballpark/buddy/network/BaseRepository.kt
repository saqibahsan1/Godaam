package ballpark.buddy.network

interface BaseRepository {
    suspend fun <T> execute(apiFunction: suspend () -> T): ApiResult<T> =
        callApi { apiFunction.invoke() }
}

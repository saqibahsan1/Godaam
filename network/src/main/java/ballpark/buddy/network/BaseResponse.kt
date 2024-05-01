package ballpark.buddy.network

import java.io.Serializable

abstract class BaseResponse<T> : Serializable {
    abstract val response: T?
    abstract val status: Int
    abstract val message: String
}

abstract class BreakDownResponse<T> : Serializable {
    abstract val entryPoint: String
    abstract val message: String
    abstract val succeeded: Boolean
}


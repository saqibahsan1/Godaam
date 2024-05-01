package ballpark.buddy.android.base.data

sealed interface AppError {
    sealed interface AccountError : AppError {
        data object UnAuthorizedUser : AccountError
        data object UserNotFound : AccountError
    }
}

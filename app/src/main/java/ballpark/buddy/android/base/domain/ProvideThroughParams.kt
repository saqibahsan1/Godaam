package ballpark.buddy.android.base.domain

interface ProvideThroughParams<Params, ReturnType> {
    fun get(params: Params): ReturnType
}

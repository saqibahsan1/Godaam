package ballpark.buddy.android.base.domain

interface Mapper<F, T> {
    fun map(from: F): T
}
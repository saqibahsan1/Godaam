package qiwa.gov.sa.extentions

import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

const val DEFAULT_INT_VALUE = 0

fun Int.lessThan(value: Int) = this < value

fun Int.lessThanEqualsTo(value: Int) = this <= value

fun Int.greaterThanEqualsTo(value: Int) = this >= value

fun Int.greaterThan(value: Int) = this > value

val Number.toSeconds
    get() = TimeUnit.SECONDS.toMillis(toLong())

fun Number.toBoolean() = this == 1

val Int?.default: Int
    get() = this ?: DEFAULT_INT_VALUE

fun Int?.default(other: Int? = null): Int =
    this ?: other ?: default

val Int?.getNonAnalyticalValue: Int
    get() = this ?: EventConstants.NON_ANALYTICAL_INTEGER

fun Float?.default(other: Float? = null): Float =
    this ?: other ?: 0.0f

fun Float?.toDecimalString(): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(this.default(0F))
}

val Float?.getNonAnalyticalValue: Float
    get() = this ?: EventConstants.NON_ANALYTICAL_FLOAT

val Long?.default
    get() = this ?: 0L

val Double?.default
    get() = this ?: 0.00

val Double?.getNonAnalyticalValue
    get() = this ?: EventConstants.NON_ANALYTICAL_DOUBLE
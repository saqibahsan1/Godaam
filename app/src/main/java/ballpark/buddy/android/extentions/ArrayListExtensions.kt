package ballpark.buddy.android.extentions

fun <T> List<T>?.orEmptyArrayList(): ArrayList<T> = emptyIfNull as ArrayList<T>

val <T> List<T>?.hasDataToShow
    get() = isNullOrEmpty().not()

fun <T> List<T>.has(index: Int): Boolean = index.lessThan(size)

fun <T> List<T>.validIndex(required: Int) = if (size.lessThan(required)) lastIndex else required

fun <T> makeList(size: Int = 5, item: T) = arrayListOf<T>().apply {
    for (i in 0 until size)
        add(item)
}

inline fun <reified T> makeList(size: Int = 5, itemCallBack: (index: Int, item: T) -> Unit) =
    arrayListOf<T>().apply {
        for (i in 0 until size)
            add(
                T::class.java.newInstance().apply {
                    itemCallBack(i, this)
                }
            )
    }

fun <T> List<T>.hasItemsMoreThan(value: Int) = size - 1 > value

fun <T> List<T>?.indexOfItem(predicate: (T) -> Boolean): Int? =
    this?.find(predicate)?.let { indexOf(it) }

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
    return map {
        if (block(it)) newValue else it
    }
}

fun <T> List<T>.replace(onIndex: Int, newValue: T): List<T> {
    return mapIndexed { index, t ->
        if (index == onIndex) newValue else t
    }
}

fun <T> List<T>.replace(newValue: T): List<T> {
    return map {
        newValue
    }
}

val <T> List<T>.firstIndex
    get() = indexOf(first())

val <T> List<T>?.emptyIfNull
    get() = this ?: arrayListOf()

fun <T> List<T>?.ifNullThen(list: List<T>) = this ?: list

fun <T> ArrayList<T>.delete(predicate: (T) -> Boolean): List<T> {
    return apply {
        find(predicate)?.let { value ->
            remove(value)
        }
    }
}

fun <T> ArrayList<T>.clearAndAddAll(elements: Collection<T>) {
    clear()
    addAll(elements)
}
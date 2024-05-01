package ballpark.buddy.android.editText

enum class ImeOptions {
    ACTION_NEXT,
    ACTION_DONE;

    companion object {
        fun get(value: Int) = entries.find { it.ordinal == value } ?: ACTION_NEXT
    }
}

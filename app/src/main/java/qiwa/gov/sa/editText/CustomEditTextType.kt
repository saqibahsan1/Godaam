package qiwa.gov.sa.editText

enum class CustomEditTextType {
    EMAIL,
    PASSWORD,
    PHONE,
    NUMBER,
    PLAIN_TEXT,
    MULTI_LINE_PLAIN_TEXT,
    PIN;

    companion object {
        fun get(value: Int) = entries.find { it.ordinal == value } ?: PLAIN_TEXT
    }
}

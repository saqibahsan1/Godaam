package qiwa.gov.sa.editText

import java.util.regex.Pattern

val PHONE_NUMBER_CHARACTERS_RANGE = 8..14

object ValidationUtilsAndExtensions {

    const val PASSWORD_REGEX =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[\$*.{}?\"!@#%&/,><\\':;|_~`^]).{8,}"
    const val EMAIL_REGEX = "^([a-zA-Z0-9+_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"

    fun areFormFieldsValid(vararg customEditTextField: CustomEditTextField) =
        customEditTextField.map { it.isValid() }.all { it }
}

val String.isValidEmailAddress
    get() = Pattern.compile(ValidationUtilsAndExtensions.EMAIL_REGEX, Pattern.CASE_INSENSITIVE)
        .matcher(this).matches()

val String.isValidPassword
    get() = Pattern.compile(ValidationUtilsAndExtensions.PASSWORD_REGEX).matcher(this).matches()

fun String.isValidPhoneNumber(countryCode: String, doInstantValidation: Boolean): Boolean {
    val hasValidLength = length + countryCode.length in PHONE_NUMBER_CHARACTERS_RANGE
    val updatedString = if (countryCode.isEmpty()) removePrefix("+") else this
    return hasValidLength && updatedString.all { it.isDigit() }
}

fun String.lengthIsInRange(minLength: Int = 1, maxLength: Int) = this.length in minLength..maxLength

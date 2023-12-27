package qiwa.gov.sa.editText

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.MovementMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.SparseArray
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.parcelize.Parcelize
import qiwa.gov.network.providers.AppLocales
import qiwa.gov.sa.R
import qiwa.gov.sa.databinding.CustomEditTextFieldBinding
import qiwa.gov.sa.extentions.EMPTY_STRING
import qiwa.gov.sa.extentions.clickToAction
import qiwa.gov.sa.extentions.colorStateList
import qiwa.gov.sa.extentions.default
import qiwa.gov.sa.extentions.drawable
import qiwa.gov.sa.extentions.getBinding
import qiwa.gov.sa.extentions.getResourceValueId
import qiwa.gov.sa.extentions.getStyleAttributesFromBinding
import qiwa.gov.sa.extentions.hide
import qiwa.gov.sa.extentions.inverse
import qiwa.gov.sa.extentions.isGone
import qiwa.gov.sa.extentions.isValidDrawableRes
import qiwa.gov.sa.extentions.isValidResourceId
import qiwa.gov.sa.extentions.show
import qiwa.gov.sa.extentions.showKeyboard
import qiwa.gov.sa.extentions.viewVisibility
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

open class CustomEditTextField @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, attributeSet, defStyleRes) {

    val binding = getBinding<CustomEditTextFieldBinding>(R.layout.custom_edit_text_field)
    private val defaultTextColor = colorStateList(R.color.black)
    private val defaultHintTextColor = colorStateList(R.color.stroke_color)
    private val primaryHintTextColor = colorStateList(R.color.text_color_dark)
    private var isValid: Boolean = false
    private var doInstantValidation: Boolean = true
    val editText: TextInputEditText by lazy {
        binding.root.findViewWithTag("editText")
    }
    val textInputLayout: TextInputLayout by lazy {
        binding.root.findViewWithTag("textInputLayout")
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState!!, editText.text.toString())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(state.superState)
        editText.setText(savedState.text)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        super.dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        super.dispatchThawSelfOnly(container)
    }

    fun getFieldText(): String =
        editText.text.toString().trim()

    var result: (Boolean, CustomEditTextErrorType) -> Unit = { _, _ -> }

    private var isOptional: Boolean = true

    private var textColor: ColorStateList = defaultTextColor
        set(value) {
            field = value
            editText.run {
                setTextColor(field)
                textInputLayout.setPrefixTextColor(field)
            }
        }

    private var prefixText: String = EMPTY_STRING
        set(value) {
            field = value
            textInputLayout.prefixText = value
        }

    private var hintTextColor: ColorStateList = defaultHintTextColor
        set(value) {
            field = value
            editText.run {
                setHintTextColor(field)
            }
        }

    private var iconRes: Int = 0
        set(value) {
            field = value
            showSupportIconIfRequired()
        }

    var text: String? = getFieldText()
        set(value) {
            field = value
            editText.setText(field)
        }

    var hintText: String? = EMPTY_STRING
        set(value) {
            field = value
            editText.hint = field
        }


    var hintTextAnimate: String? = EMPTY_STRING
        set(value) {
            field = value
            textInputLayout.hint = field
            textInputLayout.hintTextColor = defaultHintTextColor
        }


    var type: CustomEditTextType = CustomEditTextType.PLAIN_TEXT
        set(value) {
            field = value
            if (getInputType(field) != InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                editText.inputType = getInputType(field)
                if (CustomEditTextType.MULTI_LINE_PLAIN_TEXT == type) {
                    editText.isSingleLine = false
                }
            } else {
                attachIconClickListener()
            }
        }

    var imeOptions: ImeOptions = ImeOptions.ACTION_DONE
        set(value) {
            field = value
            if (CustomEditTextType.MULTI_LINE_PLAIN_TEXT != type) {
                editText.imeOptions = getImeOption(field)
            }
        }

    private var maxLength: Int = 0
        set(value) {
            field = value
            if (field > 0) {
                editText.filters += InputFilter.LengthFilter(maxLength)
            }
        }

    var fieldBackground: Int = R.drawable.edit_text_empty_background
        set(value) {
            field = value
            if (isValidResourceId(value)) {
                binding.fieldLayout.background = drawable(fieldBackground)
            }
        }

    private var editTextHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        set(value) {
            field = value
            editText.updateLayoutParams {
                height = editTextHeight
            }
        }
    var defaultErrorMessage: String = EMPTY_STRING

    var countryCode: String = EMPTY_STRING
    var enableField: Boolean = true
        set(value) {
            field = value
            editText.isEnabled = field
        }

    private var isSymbolAndNumbersAllowed: Boolean = true
        set(value) {
            field = value
            filterOutSymbolAndNumbers()
        }

    private fun filterOutSymbolAndNumbers() {
        if (isSymbolAndNumbersAllowed.inverse) {
            editText.filters += object : InputFilter {
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    for (i in start until end) {
                        source?.elementAt(i)?.let { char ->
                            return when {
                                Character.isSpaceChar(char) -> null
                                Character.isLetter(char).inverse -> EMPTY_STRING
                                else -> null
                            }
                        }
                    }
                    return null
                }
            }
        }
    }

    init {
        if (isInEditMode.inverse) {
            binding.getStyleAttributesFromBinding(R.styleable.CustomEditTextField, attributeSet) {
                iconRes =
                    getResourceValueId(this, R.styleable.CustomEditTextField_rightIconDrawable)
                isOptional = getBoolean(R.styleable.CustomEditTextField_optional, false)
                imeOptions = ImeOptions.get(getInt(R.styleable.CustomEditTextField_imeOptions, 0))
                text = getString(R.styleable.CustomEditTextField_text).default
                prefixText = getString(R.styleable.CustomEditTextField_prefixText).default
                hintText = getString(R.styleable.CustomEditTextField_hintText).default
                hintTextAnimate =
                    getString(R.styleable.CustomEditTextField_hint_text_animate).default
                maxLength = getInt(R.styleable.CustomEditTextField_maxLength, Int.MAX_VALUE)
                enableField = getBoolean(R.styleable.CustomEditTextField_android_enabled, true)
                textColor =
                    getColorStateList(R.styleable.CustomEditTextField_textColor) ?: defaultTextColor
                hintTextColor = getColorStateList(R.styleable.CustomEditTextField_textColorHint)
                    ?: defaultHintTextColor
                fieldBackground =
                    getResourceValueId(this, R.styleable.CustomEditTextField_fieldBackground)
                enableInstantValidation(
                    getBoolean(
                        R.styleable.CustomEditTextField_instantValidation,
                        false
                    )
                )
                editTextHeight = try {
                    getDimension(
                        R.styleable.CustomEditTextField_editTextHeight,
                        editTextHeight.toFloat()
                    ).toInt()
                } catch (e: Exception) {
                    editTextHeight
                }
                defaultErrorMessage =
                    getString(R.styleable.CustomEditTextField_defaultErrorMessage).default
                editText.gravity =
                    getInt(R.styleable.CustomEditTextField_android_gravity, 0)
                editText.isAllCaps =
                    getBoolean(R.styleable.CustomEditTextField_android_textAllCaps, false)
                editText.textAlignment = getInt(
                    R.styleable.CustomEditTextField_android_textAlignment,
                    TEXT_ALIGNMENT_VIEW_START
                )
                if (getBoolean(R.styleable.CustomEditTextField_change_appearance_on_focus, true)) {
                    attachFocusChangeListener()
                }
                isSymbolAndNumbersAllowed =
                    getBoolean(R.styleable.CustomEditTextField_is_numbers_and_symbols_allowed, true)
                type = CustomEditTextType.get(getInt(R.styleable.CustomEditTextField_type, 4))
            }
            attachTouchListener()
        }
    }

    private fun attachIconClickListener() {
        if (isPasswordField) {
            attachPasswordIconClickListener()
        }
    }

    private fun attachPasswordIconClickListener() {
        binding.run {
            editText.setTextVisible(false)
            rightIcon.clickToAction(withDelay = false) {
                editText.apply {
                    val hidden =
                        editText.transformationMethod == PasswordTransformationMethod.getInstance() && editText.transformationMethod != null
                    setTextVisible(hidden)
                }
            }
        }
    }

    private fun TextInputEditText.setTextVisible(hidden: Boolean) {
        transformationMethod =
            if (hidden) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        inputType =
            if (hidden) InputType.TYPE_CLASS_TEXT else InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        iconRes = if (hidden) R.drawable.ic_password else R.drawable.ic_password_hide
        setSelection(text?.length.default)
//        applyFont(R.font.roboto_regular)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun attachTouchListener() {
        editText.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }
    }

    private fun attachFocusChangeListener() {
        editText.setOnFocusChangeListener { _, hasFocus ->
            fieldBackground =
                if (hasFocus) {
                    textInputLayout.hintTextColor = primaryHintTextColor
                    R.drawable.edit_text_empty_background
                } else {
                    textInputLayout.hintTextColor = defaultHintTextColor
                    R.drawable.edit_text_empty_background
                }
        }
    }

    private fun attachTextChangeListener() {
        if (doInstantValidation) {
            editText.addTextChangedListener(
                afterTextChanged = { editable ->
                    validateTextWithInputType(editable.toString())
                }
            )
        }
    }

    private fun validateTextWithInputType(text: String) {
        val errorType: CustomEditTextErrorType = when {
            isOptional -> CustomEditTextErrorType.NONE

            type == CustomEditTextType.PLAIN_TEXT || type == CustomEditTextType.MULTI_LINE_PLAIN_TEXT -> validatePlainText(
                text
            )

            type == CustomEditTextType.PASSWORD -> checkForPasswordValidity(text)
            type == CustomEditTextType.PHONE -> validatePhoneNumber(text)
            type == CustomEditTextType.NUMBER -> validateLength(text)
            type == CustomEditTextType.EMAIL -> checkForEmailValidity(text)

            else -> CustomEditTextErrorType.NONE
        }
        isValid = errorType == CustomEditTextErrorType.NONE
        result(isValid, errorType)
    }

    private fun validatePlainText(text: String): CustomEditTextErrorType {
        val isValid = text.isNotEmpty() and text.lengthIsInRange(maxLength = maxLength)
        return if (isValid) CustomEditTextErrorType.NONE else CustomEditTextErrorType.INVALID_PLAIN_TEXT
    }

    private fun validateLength(text: String): CustomEditTextErrorType {
        val isValid = text.lengthIsInRange(maxLength = maxLength)
        return if (isValid) CustomEditTextErrorType.NONE else CustomEditTextErrorType.INVALID_LENGTH
    }

    private fun validatePhoneNumber(
        text: String
    ): CustomEditTextErrorType {
        val isValid = text.isValidPhoneNumber(countryCode, doInstantValidation)
        return if (isValid)
            CustomEditTextErrorType.NONE else CustomEditTextErrorType.INVALID_PHONE
    }

    private fun shake(view: View): AnimatorSet {
        val animatorSet = AnimatorSet()
        val object1: ObjectAnimator = ObjectAnimator.ofFloat(
            view,
            "translationX",
            0f,
            25f,
            -25f,
            25f,
            -25f,
            15f,
            -15f,
            6f,
            -6f,
            0f
        )
        animatorSet.playSequentially(object1)
        return animatorSet
    }

    private fun checkForPasswordValidity(text: String): CustomEditTextErrorType {
        return when {
            text.lengthIsInRange(maxLength = maxLength) /*&& text.isValidPassword*/ -> CustomEditTextErrorType.NONE
            text.lengthIsInRange(maxLength = maxLength).inverse -> CustomEditTextErrorType.INVALID_LENGTH
            text.isValidPassword -> CustomEditTextErrorType.INVALID_PASSWORD
            else -> CustomEditTextErrorType.NONE
        }
    }

    private fun checkForEmailValidity(text: String): CustomEditTextErrorType {
        return when {
            text.lengthIsInRange(maxLength = maxLength) && text.isValidEmailAddress -> CustomEditTextErrorType.NONE
            text.lengthIsInRange(maxLength = maxLength).inverse -> CustomEditTextErrorType.INVALID_LENGTH
            text.isValidEmailAddress.inverse -> CustomEditTextErrorType.INVALID_EMAIL
            else -> CustomEditTextErrorType.NONE
        }
    }

    fun setError(errorMessage: String, editTextField: CustomEditTextField) {
        when {
            isValid.inverse -> {
                binding.error.text = errorMessage
                showErrorViews(errorMessage.isNotEmpty())
                shake(editTextField).setDuration(800).start()
            }

            else -> showErrorViews(false)
        }
    }

    fun showErrorViews(show: Boolean) {
        binding.run {
            viewVisibility(error, show)
            when {
//                isPasswordField && show -> {
//                    attachPasswordIconClickListener()
//                }
                else -> {
                    rightIcon.imageTintList = null
                    showSupportIconIfRequired()
                }
            }
        }
    }

    private fun showSupportIconIfRequired() {
        binding.run {
            if (isValidDrawableRes(iconRes)) {
                rightIcon.apply {
                    setImageResource(iconRes)
                    rightIcon.show()
                }
            } else {
                rightIcon.hide()
            }
        }
    }

    fun setErrorExplicitly(errorMessage: String) {
        binding.error.text = errorMessage
        showErrorViews(errorMessage.isNotEmpty())
    }

    fun isValid() = when {
        isOptional -> isOptional
        isGone -> true
        else -> {
            validateTextWithInputType(getFieldText())
            isValid
        }
    }

    private fun getInputType(inputType: CustomEditTextType) =
        when (inputType) {
            CustomEditTextType.PASSWORD -> {
                textInputLayout.editText?.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            CustomEditTextType.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            CustomEditTextType.PHONE -> InputType.TYPE_CLASS_PHONE
            CustomEditTextType.NUMBER -> InputType.TYPE_CLASS_NUMBER
            CustomEditTextType.PLAIN_TEXT -> InputType.TYPE_CLASS_TEXT
            CustomEditTextType.MULTI_LINE_PLAIN_TEXT -> {
                editText.isElegantTextHeight = true
                InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }

            CustomEditTextType.PIN -> InputType.TYPE_CLASS_NUMBER
        }

    private fun getImeOption(imeOption: ImeOptions) =
        when (imeOption) {
            ImeOptions.ACTION_NEXT -> EditorInfo.IME_ACTION_NEXT
            ImeOptions.ACTION_DONE -> EditorInfo.IME_ACTION_DONE
        }

    fun isOptional(optional: Boolean) {
        isOptional = optional
    }

    private fun enableInstantValidation(enable: Boolean) {
        doInstantValidation = enable
        attachTextChangeListener()
    }

    fun setMovementMethod(movementMethod: MovementMethod) {
        editText.movementMethod = movementMethod
    }

    @Parcelize
    data class SavedState(val state: Parcelable, val text: String) : BaseSavedState(state)
}


@BindingAdapter("click_field")
fun CustomEditTextField.clickableField(listener: View.OnClickListener) {
    editText.isFocusableInTouchMode = false
    editText.isClickable = true
    editText.setOnClickListener {
        listener.onClick(this)
    }
    textInputLayout.setOnClickListener { listener.onClick(this) }
    clickToAction {
        listener.onClick(this)
    }
}

private fun getLocaleForEnglish(): Locale =
    Locale.forLanguageTag(AppLocales.English.code)

private fun parseDateTime(dateTime: String?): String {
    return if (dateTime.isNullOrEmpty()) {
        EMPTY_STRING
    } else {
        try {
            val simpleDateFormat = SimpleDateFormat(
                "dd/MM/yyyy",
                getLocaleForEnglish()
            )
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            simpleDateFormat.parse(dateTime.default)?.let { date ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatMillisToDetailedDateFormat(date.time)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
            } ?: dateTime
        } catch (e: Exception) {
            dateTime
        }
    }
}

private fun getLocaleFromLanguageTag(): Locale =
    Locale.forLanguageTag("en")

@RequiresApi(Build.VERSION_CODES.O)
private fun formatMillisToDetailedDateFormat(timeInMillis: Long): String {
    return Instant.ofEpochMilli(timeInMillis)
        .atZone(ZoneId.of(TimeZone.getDefault().id))
        .format(DateTimeFormatter.ofPattern("dd / MMMM / yyyy", getLocaleFromLanguageTag()))
}


@BindingAdapter("set_text_if_not_blank")
fun CustomEditTextField.setTextIfNotBlank(enteredText: String?) {
    enableField = true
    enteredText?.let {
        if (it.isNotEmpty()) {
            text = it
            enableField = false
        }
    }
}

@BindingAdapter("set_invalid_code_message")
fun CustomEditTextField.setInvalidCodeMessage(message: String?) {
    if (message?.isNotEmpty().default) {
        setErrorExplicitly(message.default)
    }
}

@BindingAdapter("set_validation")
fun CustomEditTextField.setValidation(message: String?) {
    result = { _, _ ->
        setError(message.default(defaultErrorMessage), this)
    }
}


@BindingAdapter("custom_color")
fun CustomEditTextField.setColor(color: Int) {
    editText.setTextColor(color);
}

@BindingAdapter("hintText")
fun CustomEditTextField.setHintText(hint: String?) {
    hintText = hint.default
}

@BindingAdapter("hint_text_animate")
fun CustomEditTextField.setHintTextAnimate(hint: String?) {
    hintTextAnimate = hint.default
}

@BindingAdapter("text")
fun CustomEditTextField.setText(textValue: String?) {
    text = textValue
}

@BindingAdapter("default_error_message")
fun CustomEditTextField.setDefaultErrorMessage(message: String?) {
    defaultErrorMessage = message.default
}

@BindingAdapter("set_translated_hint", "fall_back_hint")
fun CustomEditTextField.setTranslatedHint(translatedText: String?, fallback: String) {
    setHintText(translatedText.default(fallback))
}

@BindingAdapter("remove_error_on_typing")
fun CustomEditTextField.removeError(remove: Boolean) {
    editText.doAfterTextChanged {
        if (remove) {
            editText.error = null
            fieldBackground = R.drawable.edit_text_empty_background
            showErrorViews(false)
        }
    }
}



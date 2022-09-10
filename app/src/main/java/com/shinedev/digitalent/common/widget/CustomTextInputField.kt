package com.shinedev.digitalent.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.shinedev.digitalent.R
import com.shinedev.digitalent.common.*
import com.shinedev.digitalent.databinding.LayoutInputFieldBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("CustomViewStyleable")
class CustomTextInputField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val colorWhite by lazy { ContextCompat.getColor(context, android.R.color.white) }
    private val colorMistyLight by lazy { ContextCompat.getColor(context, R.color.colorMistyLight) }
    private var binding =
        LayoutInputFieldBinding.inflate(LayoutInflater.from(context), this)

    private var blockedOnTextChanged: Boolean = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputField)
        isFocusable = typedArray.getBoolean(R.styleable.InputField_isFocusable, true)
        setStartIcon(typedArray.getDrawable(R.styleable.InputField_startIcon))
        setText(typedArray.getString(R.styleable.InputField_android_text).orEmpty())
        setHint(typedArray.getString(R.styleable.InputField_hint).orEmpty())
        setHelper(typedArray.getString(R.styleable.InputField_helper))
        setErrorMessage(typedArray.getString(R.styleable.InputField_errorMessage))
        setInputType(
            typedArray.getInt(
                R.styleable.InputField_android_inputType,
                InputType.TYPE_CLASS_TEXT
            )
        )
        setImeOptions(
            typedArray.getInt(
                R.styleable.InputField_android_imeOptions,
                EditorInfo.IME_ACTION_NEXT
            )
        )
        setMaxLength(typedArray.getInt(R.styleable.InputField_android_maxLength, 101))
        isEnabled = typedArray.getBoolean(R.styleable.InputField_android_enabled, true)

        typedArray.recycle()
    }

    fun setMaxLength(Length: Int) {
        binding.outlinedTextField.editText?.filters = arrayOf<InputFilter>(LengthFilter(Length))
    }

    fun setText(text: String) {
        binding.etInputText.setText(text)
    }

    fun setHint(hint: String) {
        binding.outlinedTextField.hint = hint
    }

    fun setHelper(helper: String?) {
        binding.outlinedTextField.helperText = helper
    }

    fun setErrorMessage(message: String?) = with(binding) {
        if (message.isNullOrBlank()) {
            outlinedTextField.error = null
            tvError.hide()
            tvError.text = ""
        } else {
            outlinedTextField.errorIconDrawable = null
            outlinedTextField.error = " "
            if (outlinedTextField.childCount == 2) {
                outlinedTextField.getChildAt(1).hide()
            }
            tvError.show()
            tvError.text = message
        }
    }

    fun setFieldClickListener(listener: () -> Unit) = with(binding) {
        etInputText.apply {
            isFocusable = false
            setOnClickListener { listener() }
        }
    }

    fun getInputEditText(): TextInputEditText = binding.etInputText

    fun getStringText(): String = binding.etInputText.text.toString()

    fun getIsValidValue(): Boolean = binding.tvError.text.toString().isEmpty()

    fun setInputType(type: Int) = with(binding.etInputText) {
        inputType = type
        if ((type and InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            setPrimaryIcon(R.drawable.vector_eye_open) {}
            setPrimaryIconOnClick(::onPasswordIconClicked)
        } else {
            setPrimaryIcon(com.google.android.material.R.drawable.abc_ic_clear_material) {
                clearText()
            }
            setPrimaryIconOnClick(::clearText)
        }
        if (isFocusable) {
            doOnTextChanged { text, _, _, _ ->
                if ((type and InputType.TYPE_TEXT_VARIATION_PERSON_NAME) == InputType.TYPE_TEXT_VARIATION_PERSON_NAME) {
                    checkValidName(text)
                } else if ((type and InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                    checkValidEmail(text)
                } else if ((type and InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    checkValidPassword(text)
                }
                showPrimaryIcon(!text.isNullOrEmpty())
            }
        } else {
            showPrimaryIcon(false)
        }
    }

    fun clearText() = binding.etInputText.text?.clear()

    fun showPrimaryIcon(isVisible: Boolean) {
        binding.ivPrimary.isVisible = isVisible
    }

    fun setStartIcon(resId: Drawable? = null) {
        binding.outlinedTextField.startIconDrawable = resId
    }

    fun setPrimaryIconOnClick(onCLick: (() -> Unit)? = null) = with(binding) {
        ivPrimary.onClick { onCLick?.invoke() }
    }

    private fun onPasswordIconClicked() = with(binding) {
        blockedOnTextChanged = true
        val (selectionStart, selectionEnd) = Pair(
            etInputText.selectionStart,
            etInputText.selectionEnd
        )
        if (etInputText.transformationMethod is PasswordTransformationMethod) {
            ivPrimary.setImageResource(R.drawable.vector_eye_close)
            etInputText.transformationMethod = null
        } else {
            ivPrimary.setImageResource(R.drawable.vector_eye_open)
            etInputText.transformationMethod = PasswordTransformationMethod()
        }
        etInputText.setSelection(selectionStart, selectionEnd)
    }

    fun setLeadIcon(@DrawableRes resId: Int, onCLick: () -> Unit) = with(binding) {
        ivLeadIcon.show()
        ivLeadIcon.setImageResource(resId)
        ivLeadIcon.onClick { onCLick() }
    }

    fun setPrimaryIcon(@DrawableRes resId: Int, onCLick: () -> Unit) = with(binding) {
        ivPrimary.setImageResource(resId)
        ivPrimary.onClick { onCLick() }
    }

    fun setSecondaryIcon(@DrawableRes resId: Int, onCLick: () -> Unit) = with(binding) {
        ivSecondary.show()
        ivSecondary.setImageResource(resId)
        ivSecondary.onClick { onCLick() }
    }

    fun setImeOptions(option: Int) {
        binding.outlinedTextField.editText?.imeOptions = option
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.outlinedTextField.apply {
            isEnabled = enabled
            boxBackgroundColor = if (enabled) colorWhite else colorMistyLight
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            val childrenState = newState.getSparseParcelableArray<Parcelable>(SPARSE_STATE_KEY)
            childrenState?.let { restoreChildViewStates(it) }
            newState = newState.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(newState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return Bundle().apply {
            putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
            putSparseParcelableArray(SPARSE_STATE_KEY, saveChildViewStates())
        }
    }

    private fun saveChildViewStates(): SparseArray<Parcelable> = with(binding) {
        val childViewStates = SparseArray<Parcelable>()
        outlinedTextField.editText?.saveHierarchyState(childViewStates)
        return childViewStates
    }

    private fun restoreChildViewStates(childViewStates: SparseArray<Parcelable>) = with(binding) {
        outlinedTextField.editText?.restoreHierarchyState(childViewStates)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun textChangedListener(
        scope: CoroutineScope,
        delayInMillis: Long = 50L,
        listener: (String) -> Unit
    ) {
        binding.outlinedTextField.editText?.onInputText(
            scope = scope,
            delayInMillis = delayInMillis,
            listener = {
                showPrimaryIcon(it.isNotEmpty())
                listener(it)
            }
        )
    }

    private fun checkValidEmail(email: CharSequence?) {
        email.isValidEmail.also {
            val errorMessage =
                if (it or email.isNullOrBlank()) null else resources.getString(R.string.error_format_email)
            setErrorMessage(errorMessage)
        }
    }

    private fun checkValidPassword(password: CharSequence?) {
        password.isValidPassword.also {
            val errorMessage =
                if (it or password.isNullOrBlank()) null else resources.getString(R.string.error_length_password)
            setErrorMessage(errorMessage)
        }
    }

    private fun checkValidName(name: CharSequence?) {
        name.isValidName.also {
            val errorMessage =
                if (it or name.isNullOrBlank()) null else resources.getString(R.string.error_length_name)
            setErrorMessage(errorMessage)
        }
    }

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
    }
}

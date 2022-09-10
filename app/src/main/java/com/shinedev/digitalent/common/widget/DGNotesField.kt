package com.shinedev.digitalent.common.widget

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.content.res.ResourcesCompat.ID_NULL
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.CoroutineScope
/*
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("CustomViewStyleable")
class DGNotesField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding =
        LayoutBnsNotesFieldBinding.inflate(LayoutInflater.from(context), this)
    private var blockedOnTextChanged: Boolean = false

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputField)
        isFocusable = typedArray.getBoolean(R.styleable.InputField_isFocusable, true)
        isEnabled = typedArray.getBoolean(R.styleable.InputField_android_enabled, true)
        setText(typedArray.getString(R.styleable.InputField_android_text).orEmpty())
        setHint(typedArray.getString(R.styleable.InputField_hint).orEmpty())
        setLabel(typedArray.getString(R.styleable.InputField_hint).orEmpty())
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
        setPrimaryIcon(
            typedArray.getResourceId(
                R.styleable.InputField_primaryIcon,
                DEFAULT_NO_RESOURCE_ID
            )
        )
        setSecondaryIcon(
            typedArray.getResourceId(
                R.styleable.InputField_secondaryIcon,
                DEFAULT_NO_RESOURCE_ID
            )
        )
        setLeadIcon(
            typedArray.getResourceId(
                R.styleable.InputField_leadIcon,
                DEFAULT_NO_RESOURCE_ID
            )
        )
        setMaxLength(typedArray.getInt(R.styleable.InputField_android_maxLength, 255))
        typedArray.recycle()
    }

    private fun setImeOptions(option: Int) {
        binding.outlinedTextField.editText?.imeOptions = option
    }

    private fun setMaxLength(Length: Int) {
        binding.outlinedTextField.editText?.filters = arrayOf<InputFilter>(LengthFilter(Length))
    }

    private fun setHelper(helper: String?) {
        binding.outlinedTextField.helperText = helper
    }

    private fun setLeadIcon(@DrawableRes resId: Int) = with(binding) {
        if (resId == DEFAULT_NO_RESOURCE_ID) return@with
        ivLeadIcon.show()
        ivLeadIcon.setImageResource(resId)
    }

    private fun setPrimaryIcon(@DrawableRes resId: Int) = with(binding) {
        if (resId == DEFAULT_NO_RESOURCE_ID) return@with
        ivPrimary.setImageResource(resId)
    }

    private fun setSecondaryIcon(@DrawableRes resId: Int) = with(binding) {
        if (resId == DEFAULT_NO_RESOURCE_ID) return@with
        ivSecondary.show()
        ivSecondary.setImageResource(resId)
    }

    private fun setInputType(type: Int) = with(binding.etInputText) {
        inputType = type
        if ((type and InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            setPrimaryIcon(R.drawable.vector_eye_open)
            setPrimaryIconOnClick(::onPasswordIconClicked)
        } else {
            setPrimaryIcon(R.drawable.vector_clear_text_red)
            setPrimaryIconOnClick(::clearText)
        }
        // To handle when user input then show clear text or password action
        // only applicable when user can gain focus on edit text
        if (isFocusable) {
            doOnTextChanged { text, _, _, _ ->
                showPrimaryIcon(!text.isNullOrEmpty())
            }
        } else {
            showPrimaryIcon(false)
        }
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

    fun setHint(hint: String) {
        binding.outlinedTextField.hint = hint
    }

    fun setLabel(label: String) = with(binding) {
        outlinedTextField.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) outlinedTextField.hint = label
        }
    }

    fun setText(text: String) {
        binding.etInputText.setText(text)
    }

    fun setErrorMessage(message: String?) = with(binding) {
        if (message.isNullOrBlank()) {
            outlinedTextField.error = null
            tvError.hide()
            tvError.text = ""
        } else {
            outlinedTextField.error = " "
            if (outlinedTextField.childCount == 2) {
                outlinedTextField.getChildAt(1).hide()
            }
            tvError.show()
            tvError.text = message
        }
    }

    *//**
     * Call this function to treat Input Field as Dropdown or selection.
     *
     * We will always show primaryIcon (please replace icon using [setPrimaryIcon])
     *
     * @param listener Callback for what to do when user click on the input field
     *//*
    fun setFieldClickListener(listener: () -> Unit) = with(binding) {
        showPrimaryIcon(true)
        etInputText.apply {
            isCursorVisible = false
            setOnClickListener { listener() }
        }
    }

    fun getStringText(): String = binding.etInputText.text.toString()

    fun clearText() = binding.etInputText.text?.clear()

    private fun showPrimaryIcon(isVisible: Boolean) {
        binding.ivPrimary.isVisible = isVisible
    }

    fun setLeadIconOnClick(onCLick: (() -> Unit)? = null) = with(binding) {
        ivLeadIcon.onClick { onCLick?.invoke() }
    }

    fun setPrimaryIconOnClick(onCLick: (() -> Unit)? = null) = with(binding) {
        ivPrimary.onClick { onCLick?.invoke() }
    }

    fun setSecondaryIconOnClick(onCLick: (() -> Unit)? = null) = with(binding) {
        ivSecondary.onClick { onCLick?.invoke() }
    }

    fun setToAlphaNumericInputType() {
        binding.etInputText.filters = arrayOf(
            InputFilter { text, _, _, _, _, _ ->
                if (text.toString().matches(Regex("[a-zA-Z0-9 ]+"))) {
                    return@InputFilter text
                } else return@InputFilter ""
            }
        )
    }

    fun textChangedListener(
        scope: CoroutineScope,
        delayInMillis: Long = 50L,
        listener: (String) -> Unit
    ) {
        binding.outlinedTextField.editText?.onInputText(
            scope = scope,
            delayInMillis = delayInMillis,
            listener = {
                if (blockedOnTextChanged) {
                    blockedOnTextChanged = false
                } else {
                    listener(it)
                }
            }
        )
    }

    fun afterTextChanged(listener: (String) -> Unit) {
        binding.outlinedTextField.editText?.doAfterTextChanged {
            listener(it?.toString().orEmpty())
        }
    }

    fun forceCapitalizedAfterTextChanged(listener: (String) -> Unit) {
        binding.outlinedTextField.editText?.filters = arrayOf(InputFilter.AllCaps())
        afterTextChanged { listener(it) }
    }

    override fun setFocusable(focusable: Boolean) {
        super.setFocusable(focusable)
        binding.etInputText.isFocusable = focusable
    }

    override fun setEnabled(enabled: Boolean) = with(binding) {
        super.setEnabled(enabled)
        outlinedTextField.isEnabled = enabled
        outlinedTextField.boxBackgroundColor = if (enabled) {
            ContextCompat.getColor(context, android.R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.colorMistyLight)
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

    override fun onSaveInstanceState(): Parcelable {
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

    fun disableLongClick() {
        binding.etInputText.isFocusable = false
        binding.etInputText.isClickable = false
        binding.etInputText.isLongClickable = false
    }

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
        private const val DEFAULT_NO_RESOURCE_ID = ID_NULL
    }
}*/

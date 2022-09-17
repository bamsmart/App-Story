package com.shinedev.digitalent.common.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.DynamicDrawableSpan
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton

class CustomSubmitButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    private var isLoading = false
    private var savedButtonText = ""

    private val progressDrawable = CircularProgressDrawable(context).apply {
        setStyle(CircularProgressDrawable.LARGE)
        val size = (centerRadius + strokeWidth).toInt() * 2
        setBounds(0, 0, size, size)
    }

    private val drawableSpan: DynamicDrawableSpan = object : DynamicDrawableSpan() {
        override fun getDrawable() = progressDrawable
    }

    private val progressDrawableCallback: Drawable.Callback = object : Drawable.Callback {
        override fun unscheduleDrawable(who: Drawable, what: Runnable) {
            // no implementation
        }

        override fun invalidateDrawable(who: Drawable) {
            invalidate()
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
            // no implementation
        }
    }

    fun setLoading(isLoading: Boolean) {
        if (this.isLoading == isLoading) return
        this.isLoading = isLoading
        this.isClickable = !isLoading

        if (isLoading) {
            savedButtonText = text?.toString().orEmpty()
            progressDrawable.setColorSchemeColors(currentTextColor)
            progressDrawable.callback = progressDrawableCallback
            val spannableString = SpannableString(" ").apply {
                setSpan(drawableSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            text = spannableString
            progressDrawable.start()
        } else {
            progressDrawable.stop()
            progressDrawable.callback = null
            text = savedButtonText
        }
    }
}
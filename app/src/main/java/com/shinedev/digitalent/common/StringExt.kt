package com.shinedev.digitalent.common

import android.util.Patterns

inline val CharSequence?.isValidEmail: Boolean
    get() = !isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

inline val CharSequence?.isValidPassword: Boolean
    get() = !isNullOrBlank() && length > 5

inline val CharSequence?.isValidName: Boolean
    get() = !isNullOrBlank() && length > 3
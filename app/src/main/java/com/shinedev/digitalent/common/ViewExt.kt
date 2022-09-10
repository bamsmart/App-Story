package com.shinedev.digitalent.common

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide(stillHadSpace: Boolean = false) {
    visibility = if (stillHadSpace) View.INVISIBLE else View.GONE
}

inline fun <V : View> V.onClick(crossinline listener: (V) -> Unit) {
    this.setOnClickListener {
        listener.invoke(this)
    }
}
@FlowPreview
@ExperimentalCoroutinesApi
inline fun EditText.onInputText(
    scope: CoroutineScope,
    delayInMillis: Long = 50L,
    crossinline listener: (String) -> Unit
) {
    this.asFlowTextChanged().debounce(delayInMillis).onEach {
        listener(it?.toString().orEmpty())
    }.launchIn(scope)
}

@ExperimentalCoroutinesApi
fun EditText.asFlowTextChanged(): Flow<CharSequence?> = callbackFlow {
    this@asFlowTextChanged.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            // noimplementation
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // noimplementation
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            trySend(s).isSuccess
        }
    })
    awaitClose {
        // do nothing
    }
}
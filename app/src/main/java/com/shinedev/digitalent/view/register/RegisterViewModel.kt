package com.shinedev.digitalent.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinedev.digitalent.network.BaseResponse
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.view.login.service.UserAuthApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val authService = NetworkBuilder.createService(UserAuthApi::class.java)


    private val _result = MutableLiveData<BaseResponse>()
    val result: LiveData<BaseResponse> = _result

    fun signup(request: RegisterRequest) {
        viewModelScope.launch {
            val client = authService.register(request)
            client.enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {
                    if (response.isSuccessful) {
                        _result.value = response.body()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
        }
    }

    companion object {
        private const val TAG = "RegistrationViewModel"
    }
}

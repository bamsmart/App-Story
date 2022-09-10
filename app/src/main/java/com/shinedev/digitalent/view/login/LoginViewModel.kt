package com.shinedev.digitalent.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.login.service.LoginRequest
import com.shinedev.digitalent.view.login.service.UserAuthApi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: AuthPreference) : ViewModel() {

    private val authService = NetworkBuilder.createService(UserAuthApi::class.java)

    private val _result = MutableLiveData<LoginResponse>()
    val result: LiveData<LoginResponse> = _result

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            val client = authService.login(request)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        _result.value = response.body()

                        val isError = response.body()?.error ?: false
                        if (!isError) {
                            viewModelScope.launch {
                                pref.apply {
                                    setIsLogin(true)
                                    setToken(response.body()?.result?.token ?: "")
                                }
                            }
                        }
                    } else {
                        Log.e(TAG, "error")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e(TAG, "error")
                }
            })
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
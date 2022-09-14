package com.shinedev.digitalent.view.register

import androidx.lifecycle.*
import com.shinedev.digitalent.data.register.RegisterRequest
import com.shinedev.digitalent.network.BaseResponse
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.domain.authorization.UserAuthApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val authService = NetworkBuilder.createService(UserAuthApiService::class.java)

    private val _result = MutableLiveData<BaseResponse>()
    val result: LiveData<BaseResponse> = _result

    private val _isValidName = MutableLiveData<Boolean>()
    private val isValidName: LiveData<Boolean> = _isValidName

    private val _isValidEmail = MutableLiveData<Boolean>()
    private val isValidEmail: LiveData<Boolean> = _isValidEmail

    private val _isValidPassword = MutableLiveData<Boolean>()
    private val isValidPassword: LiveData<Boolean> = _isValidPassword

    private val _isValidInput: MediatorLiveData<Boolean> = MediatorLiveData()
    val isValidInput: LiveData<Boolean> = _isValidInput

    init {
        _isValidInput.addSource(isValidName) { isValidName ->
            _isValidInput.value =
                checkFormInput(
                    isValidName = isValidName,
                    isValidEmail = _isValidEmail.value ?: false,
                    isValidPassword = _isValidPassword.value ?: false
                )
        }
        _isValidInput.addSource(isValidEmail) { isValidEmail ->
            _isValidInput.value =
                checkFormInput(
                    isValidName = _isValidName.value ?: false,
                    isValidEmail = isValidEmail,
                    isValidPassword = _isValidEmail.value ?: false
                )
        }
        _isValidInput.addSource(isValidPassword) { isValidPassword ->
            _isValidInput.value =
                checkFormInput(
                    isValidName = _isValidName.value ?: false,
                    isValidEmail = _isValidEmail.value ?: false,
                    isValidPassword = isValidPassword
                )
        }
    }

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
                        _result.value = BaseResponse(error = true, message = response.message())
                    }
                }

                override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                    _result.value = BaseResponse(error = true, message = t.message.toString())
                }

            })
        }
    }

    fun isValidName(isValid: Boolean) {
        _isValidName.value = isValid
    }

    fun isValidEmail(isValid: Boolean) {
        _isValidEmail.value = isValid
    }

    fun isValidPassword(isValid: Boolean) {
        _isValidPassword.value = isValid
    }

    private fun checkFormInput(
        isValidName: Boolean,
        isValidEmail: Boolean,
        isValidPassword: Boolean
    ) =
        isValidName && isValidEmail && isValidPassword
}

class RegisterViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
